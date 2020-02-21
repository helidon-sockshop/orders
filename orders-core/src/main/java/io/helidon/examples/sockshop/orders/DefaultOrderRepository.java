package io.helidon.examples.sockshop.orders;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

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
@Alternative
@Priority(Interceptor.Priority.APPLICATION+1)
@Traced
public class DefaultOrderRepository implements OrderRepository {
    protected Map<String, Order> orders;

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
    public CompletionStage<Collection<? extends Order>> findOrdersByCustomer(String customerId) {
        return CompletableFuture.completedStage(orders.values().stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList()));
    }

    @Override
    public CompletionStage<Order> get(String orderId) {
        return CompletableFuture.completedStage(orders.get(orderId));
    }

    @Override
    public CompletionStage saveOrder(Order order) {
        orders.put(order.getOrderId(), order);
        return CompletableFuture.completedStage(null);
    }
}
