/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.mongo;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.TestOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import com.mongodb.client.MongoCollection;
import org.bson.BsonDocument;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Alternative
@Priority(APPLICATION + 5)
public class TestMongoOrderRepository extends MongoOrderRepository implements TestOrderRepository {
    @Inject
    public TestMongoOrderRepository(MongoCollection<Order> orders) {
        super(orders);
    }

    @Override
    public void clear() {
        orders.deleteMany(new BsonDocument());
    }
}
