/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.coherence;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderRepository;

import com.oracle.coherence.inject.Name;
import com.tangosol.net.NamedMap;
import com.tangosol.util.Filters;

import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses Coherence as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class CoherenceOrderRepository implements OrderRepository {
    protected NamedMap<String, Order> orders;

    @Inject
    public CoherenceOrderRepository(@Name("orders") NamedMap<String, Order> orders) {
        this.orders = orders;
    }

    @Override
    public Collection<? extends Order> findOrdersByCustomer(String customerId) {
        Collection<Order> customerOrders = orders.values(Filters.equal(o -> ((Order) o).getCustomer().getId(), customerId), null);
        return customerOrders.isEmpty() ? Collections.EMPTY_LIST : customerOrders;
    }

    @Override
    public Order get(String orderId) {
        return orders.get(orderId);
    }

    @Override
    public void saveOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }
}
