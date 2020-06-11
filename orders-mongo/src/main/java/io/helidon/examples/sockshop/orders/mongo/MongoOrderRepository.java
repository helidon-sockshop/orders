/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
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
