/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.redis;

import io.helidon.examples.sockshop.orders.OrderRepositoryTest;
import io.helidon.examples.sockshop.orders.TestOrderRepository;

import static io.helidon.examples.sockshop.orders.redis.RedisProducers.client;
import static io.helidon.examples.sockshop.orders.redis.RedisProducers.orders;

/**
 * Tests for Redis repository implementation.
 */
class RedisOrderRepositoryIT extends OrderRepositoryTest {
    public TestOrderRepository getOrderRepository() {
        String host = System.getProperty("db.host","localhost");
        int    port = Integer.parseInt(System.getProperty("db.port","6379"));

        return new TestRedisOrderRepository(orders(client(host, port)));
    }
}
