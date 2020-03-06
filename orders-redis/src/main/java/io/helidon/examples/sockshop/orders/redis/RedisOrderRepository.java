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
import io.helidon.examples.sockshop.orders.DefaultOrderRepository;

import org.eclipse.microprofile.opentracing.Traced;
import org.redisson.api.RMap;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses Redis (via Redisson) as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
@Traced
public class RedisOrderRepository extends DefaultOrderRepository {
    @Inject
    public RedisOrderRepository(RMap<String, Order> carts) {
        super(carts);
    }
}
