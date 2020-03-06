package io.helidon.examples.sockshop.orders.redis;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.TestOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import org.redisson.api.RMap;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Alternative
@Priority(APPLICATION + 5)
public class TestRedisOrderRepository extends RedisOrderRepository implements TestOrderRepository {
    @Inject
    public TestRedisOrderRepository(RMap<String, Order> orders) {
        super(orders);
    }

    @Override
    public void clear() {
        orders.clear();
    }
}
