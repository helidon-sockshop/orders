package io.helidon.examples.sockshop.orders;

import java.util.Collection;

/**
 */
public interface OrderRepository {
    Collection<? extends Order> findOrdersByCustomer(String customerId);

    Order getOrCreate(String orderId);

    void saveOrder(Order order);
}
