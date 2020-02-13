package io.helidon.examples.sockshop.orders.redis;

import java.util.Collection;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.DefaultOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import org.eclipse.microprofile.opentracing.Traced;
import org.redisson.api.RMap;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses Redis (via Redisson) as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class RedisOrderRepository extends DefaultOrderRepository {
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

    @Override
    public CompletionStage clear() {
        return carts.readAllKeySetAsync()
                    .thenApply(ks -> carts.fastRemoveAsync(ks.toArray(new String[ks.size()])));
    }
}
