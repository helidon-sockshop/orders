/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.mongo;

import io.helidon.examples.sockshop.orders.OrderRepositoryTest;
import io.helidon.examples.sockshop.orders.TestOrderRepository;

import static io.helidon.examples.sockshop.orders.mongo.MongoProducers.client;
import static io.helidon.examples.sockshop.orders.mongo.MongoProducers.db;
import static io.helidon.examples.sockshop.orders.mongo.MongoProducers.orders;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.orders.mongo.MongoOrderRepository}.
 */
class MongoOrderRepositoryIT extends OrderRepositoryTest {
    public TestOrderRepository getOrderRepository() {
        String host = System.getProperty("db.host","localhost");
        int    port = Integer.parseInt(System.getProperty("db.port","27017"));

        return new TestMongoOrderRepository(orders(db(client(host, port))));
    }
}
