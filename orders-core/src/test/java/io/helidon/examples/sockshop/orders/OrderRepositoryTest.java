package io.helidon.examples.sockshop.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.helidon.examples.sockshop.orders.TestDataFactory.order;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Abstract base class containing tests for all
 * {@link io.helidon.examples.sockshop.orders.OrderRepository} implementations.
 */
public abstract class OrderRepositoryTest {
    private OrderRepository orders = getOrderRepository();

    protected abstract OrderRepository getOrderRepository();
    protected abstract void clearRepository(OrderRepository orders);

    @BeforeEach
    void setup() {
        clearRepository(orders);
    }

    @Test
    void testFindOrdersByCustomer() {
        orders.saveOrder(order("homer", 1)).toCompletableFuture().join();
        orders.saveOrder(order("homer", 2)).toCompletableFuture().join();
        orders.saveOrder(order("marge", 5)).toCompletableFuture().join();

        assertThat(orders.findOrdersByCustomer("homer").toCompletableFuture().join().size(), is(2));
        assertThat(orders.findOrdersByCustomer("marge").toCompletableFuture().join().size(), is(1));
    }

    @Test
    void testOrderCreation() {
        Order order = order("homer", 1);
        orders.saveOrder(order).toCompletableFuture().join();

        assertThat(orders.get(order.getOrderId()).toCompletableFuture().join(), is(order));
    }

}
