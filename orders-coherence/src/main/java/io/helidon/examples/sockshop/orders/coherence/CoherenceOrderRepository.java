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

package io.helidon.examples.sockshop.orders.coherence;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderRepository;

import com.oracle.coherence.cdi.Cache;
import com.tangosol.net.NamedCache;
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
    protected NamedCache<String, Order> orders;

    @Inject
    public CoherenceOrderRepository(@Cache("orders") NamedCache<String, Order> orders) {
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
