package io.helidon.examples.sockshop.orders.redis;

import io.helidon.examples.sockshop.orders.OrderRepository;
import io.helidon.examples.sockshop.orders.OrderRepositoryTest;

import static io.helidon.examples.sockshop.orders.redis.RedisProducers.client;
import static io.helidon.examples.sockshop.orders.redis.RedisProducers.orders;

/**
 * Tests for Redis repository implementation.
 */
class RedisOrderRepositoryIT extends OrderRepositoryTest {
    public OrderRepository getOrderRepository() {
        String host = System.getProperty("db.host","localhost");
        int    port = Integer.parseInt(System.getProperty("db.port","6379"));

        return new TestRedisOrderRepository(orders(client(host, port)));
    }
}
