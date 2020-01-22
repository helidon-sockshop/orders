package io.helidon.examples.sockshop.orders;

import java.net.URI;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@ApplicationScoped
@Path("/orders")
public class OrderResource {

    @Inject
    private OrderRepository orders;

    private static final Logger LOGGER = Logger.getLogger(OrderResource.class.getName());
    private static final Client CLIENT = ClientBuilder.newClient();

    @ConfigProperty(name = "http.timeout")
    private long timeout;

    @GET
    @Path("search/customerId")
    @Produces(APPLICATION_JSON)
    public Response getOrdersForCustomer(@QueryParam("custId") String customerId) {
        Collection<? extends Order> customerOrders = orders.findOrdersByCustomer(customerId);
        if (customerOrders.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return wrap("customerOrders", customerOrders);
    }

    private Response wrap(String key, Object value) {
        Map<String, Map<String, Object>> map = Collections.singletonMap("_embedded", Collections.singletonMap(key, value));
        return Response.ok(map).build();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Order getOrder(@PathParam("id") String orderId) {
        return orders.getOrCreate(orderId);
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response newOrder(@Context UriInfo uriInfo, NewOrderRequest request) {
        if (request.address == null || request.customer == null || request.card == null || request.items == null) {
            throw new InvalidOrderException("Invalid order request. Order requires customer, address, card and items.");
        }

        LOGGER.log(Level.INFO, "Processing new order...");

        List<Item> items    = httpGet(request.items, new GenericType<List<Item>>() { });
        Address    address  = httpGet(request.address, Address.class);
        Card       card     = httpGet(request.card, Card.class);
        Customer   customer = httpGet(request.customer, Customer.class);

        float amount = calculateTotal(items);

        // Call payment service to make sure they've paid
        LOGGER.log(Level.INFO, "Calling Payment service ...");

        String orderId = UUID.randomUUID().toString();
        PaymentRequest paymentRequest = new PaymentRequest(orderId, address, card, customer, amount);
        URI paymentUri = new ServiceUri(new Hostname("payment"), new Domain(""), "/paymentAuth").toUri();

        PaymentResponse paymentResponse = httpPost(paymentUri, Entity.json(paymentRequest), PaymentResponse.class);
        if (paymentResponse == null) {
            throw new PaymentDeclinedException("Unable to parse authorisation packet");
        }
        if (!paymentResponse.isAuthorised()) {
            throw new PaymentDeclinedException(paymentResponse.getMessage());
        }

        // create shipment
        LOGGER.log(Level.INFO, "Creating Shipment ...");

        String customerId = customer.getId();
        URI shippingUri = new ServiceUri(new Hostname("shipping"), new Domain(""), "/shipping").toUri();

        Shipment shipment = httpPost(shippingUri, Entity.json(new Shipment(orderId)), Shipment.class);
        LOGGER.log(Level.INFO, "Created Shipment: " + shipment);

        Link link = Link.fromMethod(OrderResource.class, "getOrder")
                .baseUri("http://orders/orders/")
                .rel("self")
                .build(orderId);

        Order order = new Order(
                orderId,
                customerId,
                customer,
                address,
                card,
                items,
                shipment,
                Calendar.getInstance().getTime(),
                amount);

        order.addLink("self", link);

        orders.saveOrder(order);

        LOGGER.log(Level.INFO, "Created Order: " + orderId);
        return Response.status(CREATED).entity(order).build();
    }

    // helper methods

    private float calculateTotal(List<Item> items) {
        float amount = 0F;
        float shipping = 4.99F;
        amount += items.stream().mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();
        amount += shipping;
        return amount;
    }

    private <T> T httpGet(URI uri, Class<T> responseClass){
        return CLIENT
                .target(uri)
                .request(APPLICATION_JSON)
                .get(responseClass);
    }

    private <T> T httpGet(URI uri, GenericType<T> responseClass){
        return CLIENT
                .target(uri)
                .request(APPLICATION_JSON)
                .get(responseClass);
    }

    private <T> T httpPost(URI uri, Entity<?> entity, Class<T> responseClass) {
        return CLIENT
                .target(uri)
                .request(APPLICATION_JSON)
                .post(entity, responseClass);
    }

    // helper classes

    private class Domain {
        private final String domain;

        private Domain(String domain) {
            this.domain = domain;
        }

        @Override
        public String toString() {
            if (domain != null && !domain.equals("")) {
                return "." + domain;
            } else {
                return "";
            }
        }
    }

    private class Hostname {
        private final String hostname;

        private Hostname(String hostname) {
            this.hostname = hostname;
        }

        @Override
        public String toString() {
            if (hostname != null && !hostname.equals("")) {
                return hostname;
            } else {
                return "";
            }
        }
    }

    private class ServiceUri {
        private final Hostname hostname;
        private final Domain domain;
        private final String endpoint;

        private ServiceUri(Hostname hostname, Domain domain, String endpoint) {
            this.hostname = hostname;
            this.domain = domain;
            this.endpoint = endpoint;
        }

        public URI toUri() {
            return URI.create(wrapHTTP(hostname.toString() + domain.toString()) + endpoint);
        }

        private String wrapHTTP(String host) {
            return "http://" + host;
        }

        @Override
        public String toString() {
            return "ServiceUri{" +
                    "hostname=" + hostname +
                    ", domain=" + domain +
                    '}';
        }
    }

    public static class OrderException extends IllegalStateException {
        public OrderException(String s) {
            super(s);
        }
    }

    public static class InvalidOrderException extends OrderException {
        public InvalidOrderException(String s) {
            super(s);
        }
    }

    public static class PaymentDeclinedException extends OrderException {
        public PaymentDeclinedException(String s) {
            super(s);
        }
    }
}
