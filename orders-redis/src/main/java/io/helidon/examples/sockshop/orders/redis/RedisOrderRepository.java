package io.helidon.examples.sockshop.orders.redis;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.DefaultOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import org.redisson.api.RMap;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses Redis (via Redisson) as a backend data store.
 */
@ApplicationScoped
@Specializes
public class RedisOrderRepository extends DefaultOrderRepository {
    @Inject
    public RedisOrderRepository(RMap<String, Order> carts) {
        super(carts);
    }
}
