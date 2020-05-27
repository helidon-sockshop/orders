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

import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.inject.Alternative;

import io.helidon.examples.sockshop.orders.DefaultOrderProcessor;
import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderProcessor;

import com.oracle.coherence.cdi.events.CacheName;
import com.oracle.coherence.cdi.events.Inserted;
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
    void onOrderCreated(@ObservesAsync @Inserted @CacheName("orders") EntryEvent<?, ?> event) {
        ((EntryEvent<String, Order>) event).getEntrySet().stream()
                .map(Map.Entry::getValue)
                .forEach(this::transitionOrder);
    }

    @SuppressWarnings("unchecked")
    void onOrderUpdated(@ObservesAsync @Updated @CacheName("orders") EntryEvent<?, ?> event) {
        ((EntryEvent<String, Order>) event).getEntrySet().stream()
                .map(Map.Entry::getValue)
                .forEach(this::transitionOrder);
    }

    private void transitionOrder(Order order) {
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
