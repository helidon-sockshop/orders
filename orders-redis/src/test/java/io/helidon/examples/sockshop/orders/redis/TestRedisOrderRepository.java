package io.helidon.examples.sockshop.orders.redis;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import io.helidon.examples.sockshop.orders.TestOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import org.redisson.api.RMap;

@Alternative
@Priority(Interceptor.Priority.APPLICATION+100)
public class TestRedisOrderRepository extends RedisOrderRepository implements TestOrderRepository {
    @Inject
    public TestRedisOrderRepository(RMap<String, Order> carts) {
        super(carts);
    }

    @Override
    public void clear() {
        carts.readAllKeySetAsync()
             .thenCompose(ks -> carts.fastRemoveAsync(ks.toArray(new String[ks.size()])))
             .toCompletableFuture()
             .join();
    }
}
