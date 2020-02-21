package io.helidon.examples.sockshop.orders.redis;

import java.util.Collection;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderRepository;

import org.eclipse.microprofile.opentracing.Traced;
import org.redisson.api.RMap;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses Redis (via Redisson) as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION+10)
@Traced
public class RedisOrderRepository implements OrderRepository {
    protected RMap<String, Order> carts;

    @Inject
    public RedisOrderRepository(RMap<String, Order> carts) {
        this.carts = carts;
    }

    @Override
    public CompletionStage<Collection<? extends Order>> findOrdersByCustomer(String customerId) {
        return carts.readAllValuesAsync()
                    .thenApply(vs -> vs.stream()
                                     .filter(order -> order.getCustomer().getId().equals(customerId))
                                     .collect(Collectors.toList()));
    }

    @Override
    public CompletionStage<Order> get(String orderId) {
        return carts.getAsync(orderId);
    }

    @Override
    public CompletionStage saveOrder(Order order) {
        return carts.putAsync(order.getOrderId(), order);
    }
}
