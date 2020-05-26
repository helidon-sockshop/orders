/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
