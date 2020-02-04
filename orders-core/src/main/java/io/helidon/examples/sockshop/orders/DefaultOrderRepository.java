package io.helidon.examples.sockshop.orders;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.opentracing.Traced;

/**
 * Simple in-memory implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that can be used for demos and testing.
 * <p/>
 * This implementation is obviously not suitable for production use, because it is not
 * persistent and it doesn't scale, but it is trivial to write and very useful for the
 * API testing and quick demos.
 */
@ApplicationScoped
@Traced
public class DefaultOrderRepository implements OrderRepository {
    private Map<String, Order> orders;

    /**
     * Construct {@code DefaultOrderRepository} with empty storage map.
     */
    public DefaultOrderRepository() {
        this(new ConcurrentHashMap<>());
    }

    /**
     * Construct {@code DefaultOrderRepository} with the specified storage map.
     *
     * @param orders the storage map to use
     */
    protected DefaultOrderRepository(Map<String, Order> orders) {
        this.orders = orders;
    }

    @Override
    public Collection<? extends Order> findOrdersByCustomer(String customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public Order get(String orderId) {
        return orders.get(orderId);
    }

    @Override
    public void saveOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    // ---- helpers ---------------------------------------------------------

    /**
     * Helper to clear this repository for testing.
     */
    public void clear() {
        orders.clear();
    }
}
