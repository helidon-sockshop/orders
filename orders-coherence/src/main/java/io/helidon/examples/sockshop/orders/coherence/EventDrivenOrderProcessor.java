/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.coherence;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.inject.Alternative;

import io.helidon.examples.sockshop.orders.DefaultOrderProcessor;
import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderProcessor;

import com.oracle.coherence.cdi.events.Inserted;
import com.oracle.coherence.cdi.events.MapName;
import com.oracle.coherence.cdi.events.Updated;
import com.tangosol.net.events.partition.cache.EntryEvent;

import lombok.extern.java.Log;

import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * A more realistic implementation of {@link OrderProcessor} that stores
 * submitted order immediately and uses Coherence server-side events
 * to process payment and ship the order asynchronously, based on the
 * order status.
 */
@Log
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class EventDrivenOrderProcessor extends DefaultOrderProcessor {
    @Override
    public void processOrder(Order order) {
        saveOrder(order);
    }

    @SuppressWarnings("unchecked")
    void onOrderCreated(@ObservesAsync @Inserted @Updated @MapName("orders") EntryEvent<String, Order> event) {
        Order order = event.getValue();

        switch (order.getStatus()) {
        case CREATED:
            try {
                processPayment(order);
            }
            finally {
                saveOrder(order);
            }
            break;

        case PAID:
            try {
                shipOrder(order);
            }
            finally {
                saveOrder(order);
            }
            break;

        default:
            // do nothing, order is in a terminal state already
        }
    }
}
