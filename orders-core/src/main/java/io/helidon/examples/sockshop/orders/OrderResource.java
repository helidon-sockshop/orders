package io.helidon.examples.sockshop.orders;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private String paymentHost;

    /**
     * Inject shipping service host[:port] from configuration.
     */
    @ConfigProperty(name = "shipping.host", defaultValue = "shipping")
    private String shippingHost;

    @GET
    @Path("search/customerId")
    @Produces(APPLICATION_JSON)
    public Response getOrdersForCustomer(@QueryParam("custId") String customerId) {
        Collection<? extends Order> customerOrders = orders.findOrdersByCustomer(customerId);
        if (customerOrders.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return wrap(customerOrders);
    }

    private Response wrap(Object value) {
        Map<String, Map<String, Object>> map = Collections.singletonMap("_embedded", Collections.singletonMap("customerOrders", value));
        return Response.ok(map).build();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response getOrder(@PathParam("id") String orderId) {
        Order order = orders.get(orderId);
        return order == null
                ? Response.status(NOT_FOUND).build()
                : Response.ok(order).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response newOrder(@Context UriInfo uriInfo, NewOrderRequest request) {
        log.info("Processing new order: " + request);

        if (request.address == null || request.customer == null || request.card == null || request.items == null) {
            throw new InvalidOrderException("Invalid order request. Order requires customer, address, card and items.");
        }

        List<Item> items    = httpGet(request.items, new GenericType<List<Item>>() { });
        Address    address  = httpGet(request.address, Address.class);
        Card       card     = httpGet(request.card, Card.class);
        Customer   customer = httpGet(request.customer, Customer.class);

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
        Payment payment = httpPost(paymentUri, Entity.json(paymentRequest), Payment.class);

        log.info("Payment processed: " + payment);

        if (payment == null) {
            throw new PaymentDeclinedException("Unable to parse authorization packet");
        }
        if (!payment.isAuthorised()) {
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
        Shipment shipment = httpPost(shippingUri, Entity.json(shippingRequest), Shipment.class);

        log.info("Created Shipment: " + shipment);

        Order order = Order.builder()
                .orderId(orderId)
                .date(LocalDateTime.now())
                .customer(customer)
                .address(address)
                .card(card)
                .items(items)
                .payment(payment)
                .shipment(shipment)
                .total(amount)
                .build();
        order.getItems().forEach(item -> item.setOrder(order));

        orders.saveOrder(order);

        log.info("Created Order: " + orderId);
        return Response.status(CREATED).entity(order).build();
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
    private <T> T httpGet(URI uri, Class<T> responseClass){
        return client
                .target(uri)
                .request(APPLICATION_JSON)
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
    private <T> T httpGet(URI uri, GenericType<T> responseClass){
        return client
                .target(uri)
                .request(APPLICATION_JSON)
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
    private <T> T httpPost(URI uri, Entity<?> entity, Class<T> responseClass) {
        return client
                .target(uri)
                .request(APPLICATION_JSON)
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
