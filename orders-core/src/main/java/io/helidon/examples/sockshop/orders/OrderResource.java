package io.helidon.examples.sockshop.orders;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import javax.ws.rs.core.Context;
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
public class OrderResource implements OrderApi{
    /**
     * Order repository to use.
     */
    @Inject
    private OrderRepository orders;

    @Inject
    protected ShippingClient shippingService;

    @Inject
    protected PaymentClient paymentService;

    @Inject
    protected CartsClient cartsService;

    @Inject
    protected UsersClient usersService;

    @Override
    public Response getOrdersForCustomer(String customerId) {
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

    @Override
    public Response getOrder(String orderId) {
        Order order = orders.get(orderId);
        return order == null
                ? Response.status(NOT_FOUND).build()
                : Response.ok(order).build();
    }

    @Override
    public Response newOrder(UriInfo uriInfo, NewOrderRequest request) {
        log.info("Processing new order: " + request);

        if (request.address == null || request.customer == null || request.card == null || request.items == null) {
            throw new InvalidOrderException("Invalid order request. Order requires customer, address, card and items.");
        }

        String itemsPath = request.items.getPath();
        String addressPath = request.address.getPath();
        String cardPath = request.card.getPath();
        String customerPath = request.customer.getPath();
        if (!itemsPath.startsWith("/carts/") || !itemsPath.endsWith("/items") ||
            !addressPath.startsWith("/addresses/") ||
            !cardPath.startsWith("/cards/") ||
            !customerPath.startsWith("/customers/")) {
            throw new InvalidOrderException("Invalid order request. Order requires the URIs to have path /customers/xxx, /addresses/xxx, /cards/xxx and /carts/xxx/items.");
        }

        List<Item> items    = cartsService.cart(itemsPath.substring(7, itemsPath.length() - 6));
        Address    address  = usersService.address(addressPath.substring(11));
        Card       card     = usersService.card(cardPath.substring(7));
        Customer   customer = usersService.customer(customerPath.substring(11));

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

        Payment payment = paymentService.authorize(paymentRequest);

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

        Shipment shipment = shippingService.ship(shippingRequest);

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
