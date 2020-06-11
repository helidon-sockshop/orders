/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

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
    private TestOrderRepository orders = getOrderRepository();

    protected abstract TestOrderRepository getOrderRepository();

    @BeforeEach
    void setup() {
        orders.clear();
    }

    @Test
    void testFindOrdersByCustomer() {
        orders.saveOrder(order("homer", 1));
        orders.saveOrder(order("homer", 2));
        orders.saveOrder(order("marge", 5));

        assertThat(orders.findOrdersByCustomer("homer").size(), is(2));
        assertThat(orders.findOrdersByCustomer("marge").size(), is(1));
    }

    @Test
    void testOrderCreation() {
        Order order = order("homer", 1);
        orders.saveOrder(order);

        assertThat(orders.get(order.getOrderId()), is(order));
    }

}
