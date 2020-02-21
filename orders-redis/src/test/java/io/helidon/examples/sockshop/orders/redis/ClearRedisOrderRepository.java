package io.helidon.examples.sockshop.orders.redis;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import io.helidon.examples.sockshop.orders.ClearOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import org.redisson.api.RMap;

@Alternative
@Priority(Interceptor.Priority.APPLICATION+100)
public class ClearRedisOrderRepository extends RedisOrderRepository implements ClearOrderRepository {
    @Inject
    public ClearRedisOrderRepository(RMap<String, Order> carts) {
        super(carts);
    }

    @Override
    public CompletionStage clear() {
        return clearRepository(this);
    }

    public static CompletionStage clearRepository(RedisOrderRepository ror) {
        return ror.carts.readAllKeySetAsync()
                        .thenApply(ks -> ror.carts.fastRemoveAsync(ks.toArray(new String[ks.size()])));
    }
}
