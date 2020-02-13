package io.helidon.examples.sockshop.orders.mongo;

import io.helidon.examples.sockshop.orders.OrderRepository;
import io.helidon.examples.sockshop.orders.OrderRepositoryTest;

import static io.helidon.examples.sockshop.orders.mongo.MongoProducers.client;
import static io.helidon.examples.sockshop.orders.mongo.MongoProducers.db;
import static io.helidon.examples.sockshop.orders.mongo.MongoProducers.orders;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.orders.mongo.MongoOrderRepository}.
 */
class MongoOrderRepositoryIT extends OrderRepositoryTest {
    public OrderRepository getOrderRepository() {
        String host = System.getProperty("db.host","localhost");
        int    port = Integer.parseInt(System.getProperty("db.port","27017"));

        return new MongoOrderRepository(orders(db(client(host, port))));
    }

    @Override
    protected void clearRepository(OrderRepository orders) {
        ((MongoOrderRepository) orders).clear().toCompletableFuture().join();
    }
}
