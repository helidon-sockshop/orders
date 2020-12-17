/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.coherence;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.TestOrderRepository;

import com.oracle.coherence.inject.Name;
import com.tangosol.net.NamedMap;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Alternative
@Priority(APPLICATION + 5)
public class TestCoherenceOrderRepository extends CoherenceOrderRepository
        implements TestOrderRepository {
    private String lastOrderId;

    @Inject
    public TestCoherenceOrderRepository(@Name("orders") NamedMap<String, Order> orders) {
        super(orders);
    }

    public void clear() {
        orders.clear();
    }

    @Override
    public void saveOrder(Order order) {
        super.saveOrder(order);
        lastOrderId = order.getOrderId();
    }

    @Override
    public String getLastOrderId() {
        return lastOrderId;
    }
}
