package io.helidon.examples.sockshop.orders;

import java.util.Collection;
import java.util.concurrent.CompletionStage;

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
    CompletionStage<Collection<? extends Order>> findOrdersByCustomer(String customerId);

    /**
     * Get an existing order.
     *
     * @param orderId the order identifier to get the order for
     *
     * @return an existing order, or {@code null} if the specified order
     *         does not exist
     */
    CompletionStage<Order> get(String orderId);

    /**
     * Save order.
     *
     * @param order the order to save
     */
    CompletionStage saveOrder(Order order);
}
