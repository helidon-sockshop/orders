package io.helidon.examples.sockshop.orders;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Implementation of the Orders Service REST API.
 */
@ApplicationScoped
@Path("/orders")
@Log
public class OrderResource {
    /**
     * HTTP client to use when calling other services.
     */
    @Inject
    private Client client;

    /**
     * Order repository to use.
     */
    @Inject
    private OrderRepository orders;

    /**
     * Inject payment service host[:port] from configuration.
     */
    @ConfigProperty(name = "payment.host", defaultValue = "payment")
    private String paymentHost = "payment";

    /**
     * Inject shipping service host[:port] from configuration.
     */
    @ConfigProperty(name = "shipping.host", defaultValue = "shipping")
    private String shippingHost = "shipping";

    @GET
    @Path("search/customerId")
    @Produces(APPLICATION_JSON)
    public void getOrdersForCustomer(@QueryParam("custId") String customerId, @Suspended AsyncResponse response) {
        orders.findOrdersByCustomer(customerId)
              .thenApply(customerOrders -> {
                  if (customerOrders.isEmpty()) {
                      return response.resume(Response.status(NOT_FOUND).build());
                  }

                  return response.resume(wrap(customerOrders));
              })
              .exceptionally(response::resume);
    }

    private Map<String, Map<String, Object>> wrap(Object value) {
        return Collections.singletonMap("_embedded", Collections.singletonMap("customerOrders", value));
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public void getOrder(@PathParam("id") String orderId, @Suspended AsyncResponse response) {
        orders.get(orderId)
              .thenApply(order -> {
                  if (order == null) {
                      return response.resume(Response.status(NOT_FOUND).build());
                  }
                  return response.resume(order);
              })
              .exceptionally(response::resume);
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public void newOrder(@Context UriInfo uriInfo, NewOrderRequest request, @Suspended AsyncResponse response) {
        log.info("Processing new order: " + request);

        if (request.address == null || request.customer == null || request.card == null || request.items == null) {
            response.resume(new InvalidOrderException("Invalid order request. Order requires customer, address, card and items."));
            return;
        }

        CompletableFuture<List<Item>> csitems    = httpGet(request.items, new GenericType<List<Item>>() { }).toCompletableFuture();
        CompletableFuture<Address>    csaddress  = httpGet(request.address, Address.class).toCompletableFuture();
        CompletableFuture<Card>       cscard     = httpGet(request.card, Card.class).toCompletableFuture();
        CompletableFuture<Customer>   cscustomer = httpGet(request.customer, Customer.class).toCompletableFuture();

        CompletableFuture.allOf(csitems, csaddress, cscard, cscustomer).thenCompose((_void) -> {
           List<Item> items = csitems.join();
           Address address = csaddress.join();
           Card card = cscard.join();
           Customer customer = cscustomer.join();

           String orderId = UUID.randomUUID().toString().substring(0, 8);
           float  amount  = calculateTotal(items);

           // Process payment
           PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderId)
                .customer(customer)
                .address(address)
                .card(card)
                .amount(amount)
                .build();

           log.info("Processing Payment: " + paymentRequest);

           URI paymentUri = URI.create(format("http://%s/payments", paymentHost));
           return httpPost(paymentUri, Entity.json(paymentRequest), Payment.class).thenCompose(payment -> {
              log.info("Payment processed: " + payment);

              if (payment == null) {
                  // Is it helidon, Jersey, or not supported? ExceptionMapper does not kick in
                  response.resume(Response.status(406)
                                          .entity(Collections.singletonMap("message", "Unable to parse authorization packet"))
                                          .type(APPLICATION_JSON)
                                          .build());
                  throw new PaymentDeclinedException("Unable to parse authorization packet");
              }
              if (!payment.isAuthorised()) {
                  // Is it helidon, Jersey, or not supported? ExceptionMapper does not kick in
                  response.resume(Response.status(406)
                                          .entity(Collections.singletonMap("message", payment.getMessage()))
                                          .type(APPLICATION_JSON)
                                          .build());
                  throw new PaymentDeclinedException(payment.getMessage());
              }

              // Create shipment
              ShippingRequest shippingRequest = ShippingRequest.builder()
                .orderId(orderId)
                .customer(customer)
                .address(address)
                .itemCount(items.size())
                .build();

              log.info("Creating Shipment: " + shippingRequest);

              URI shippingUri = URI.create(format("http://%s/shipping", shippingHost));
              return httpPost(shippingUri, Entity.json(shippingRequest), Shipment.class).thenCompose(shipment -> {
                 log.info("Created Shipment: " + shipment);

                 Order order = Order.builder()
                   .orderId(orderId)
                   .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                   .customer(customer)
                   .address(address)
                   .card(card)
                   .items(items)
                   .payment(payment)
                   .shipment(shipment)
                   .total(amount)
                   .build();
                 order.getItems().forEach(item -> item.setOrder(order));

                 return orders.saveOrder(order).thenApply((_ignore) -> {
                    log.info("Created Order: " + orderId);
                    return Response.status(CREATED).entity(order).build();
                 });
              });
           });
        })
           .thenApply(response::resume)
           .exceptionally(response::resume);
    }

    // ---- helper methods --------------------------------------------------

    /**
     * Calculate order total.
     *
     * @param items order items to calculate the total for
     *
     * @return order total, including shipping
     */
    private float calculateTotal(List<Item> items) {
        float amount = 0F;
        float shipping = 4.99F;
        amount += items.stream().mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();
        amount += shipping;
        return amount;
    }

    /**
     * Perform HTTP GET against specified URI.
     *
     * @param uri           the URI to GET
     * @param responseClass the class to convert the response to
     * @param <T>           the type of the response to return
     *
     * @return response from an HTTP GET, converted to {@code responseClass}
     */
    private <T> CompletionStage<T> httpGet(URI uri, Class<T> responseClass){
        log.info("GET " + uri);
        return client
                .target(uri)
                .request(APPLICATION_JSON)
                .rx()
                .get(responseClass);
    }

    /**
     * Perform HTTP GET against specified URI.
     *
     * @param uri           the URI to GET
     * @param responseClass the generic type to convert the response to
     * @param <T>           the type of the response to return
     *
     * @return response from an HTTP GET, converted to {@code responseClass}
     */
    private <T> CompletionStage<T> httpGet(URI uri, GenericType<T> responseClass){
        log.info("GET " + uri);
        return client
                .target(uri)
                .request(APPLICATION_JSON)
                .rx()
                .get(responseClass);
    }

    /**
     * Perform HTTP POST against specified URI.
     *
     * @param uri           the URI to POST to
     * @param responseClass the class to convert the response to
     * @param <T>           the type of the response to return
     *
     * @return response from an HTTP POST, converted to {@code responseClass}
     */
    private <T> CompletionStage<T> httpPost(URI uri, Entity<?> entity, Class<T> responseClass) {
        log.info("POST " + uri);
        return client
                .target(uri)
                .request(APPLICATION_JSON)
                .rx()
                .post(entity, responseClass);
    }

    /**
     * Base class for all business-level order processing exceptions.
     */
    public static class OrderException extends IllegalStateException {
        public OrderException(String s) {
            super(s);
        }
    }

    /**
     * An exception that is thrown if the arguments in the {@code NewOrderRequest}
     * are invalid.
     */
    public static class InvalidOrderException extends OrderException {
        public InvalidOrderException(String s) {
            super(s);
        }
    }

    /**
     * An exception that is thrown if the payment is declined.
     */
    public static class PaymentDeclinedException extends OrderException {
        public PaymentDeclinedException(String s) {
            super(s);
        }
    }
}
