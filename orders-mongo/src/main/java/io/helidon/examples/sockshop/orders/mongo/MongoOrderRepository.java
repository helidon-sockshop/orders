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

package io.helidon.examples.sockshop.orders.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderRepository;

import com.mongodb.client.MongoCollection;
import org.eclipse.microprofile.opentracing.Traced;

import static com.mongodb.client.model.Filters.eq;
import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses MongoDB as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class MongoOrderRepository implements OrderRepository {
    protected MongoCollection<Order> orders;

    @Inject
    MongoOrderRepository(MongoCollection<Order> orders) {
        this.orders = orders;
    }

    @Override
    public Order get(String orderId) {
        return orders.find(eq("orderId", orderId)).first();
    }

    @Override
    public Collection<? extends Order> findOrdersByCustomer(String customerId) {
        ArrayList<Order> results = new ArrayList<>();

        orders.find(eq("customer._id", customerId))
                .forEach((Consumer<? super Order>) results::add);

        return results;
    }

    @Override
    public void saveOrder(Order order) {
        orders.insertOne(order);
    }
}
