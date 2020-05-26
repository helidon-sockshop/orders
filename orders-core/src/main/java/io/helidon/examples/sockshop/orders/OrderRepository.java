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

import java.util.Collection;

/**
 * A repository interface that should be implemented by
 * the various data store integrations.
 */
public interface OrderRepository {
    /**
     * Find all orders for the specified customer.
     *
     * @param customerId the customer to find the orders for
     *
     * @return all orders for the specified customer
     */
    Collection<? extends Order> findOrdersByCustomer(String customerId);

    /**
     * Get an existing order.
     *
     * @param orderId the order identifier to get the order for
     *
     * @return an existing order, or {@code null} if the specified order
     *         does not exist
     */
    Order get(String orderId);

    /**
     * Save order.
     *
     * @param order the order to save
     */
    void saveOrder(Order order);
}
