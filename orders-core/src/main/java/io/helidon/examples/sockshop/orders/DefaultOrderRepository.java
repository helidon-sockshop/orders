package io.helidon.examples.sockshop.orders;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

/**
 */
@ApplicationScoped
public class DefaultOrderRepository implements OrderRepository {
    private Map<String, Order> orders = new ConcurrentHashMap<>();

    @Override
    public Collection<? extends Order> findOrdersByCustomer(String customerId) {
        return orders.values().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public Order getOrCreate(String orderId) {
        return orders.getOrDefault(orderId, new Order());
    }

    @Override
    public void saveOrder(Order order) {
        orders.put(order.getId(), order);
    }
}
