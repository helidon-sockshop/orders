package io.helidon.examples.sockshop.orders;

/**
 * Tests for default in memory repository implementation.
 */
public class DefaultOrderRepositoryTest extends OrderRepositoryTest {
    @Override
    protected OrderRepository getOrderRepository() {
        return new DefaultOrderRepository();
    }

    @Override
    protected void clearRepository(OrderRepository orders) {
        ClearDefaultOrderRepository.clearRepository((DefaultOrderRepository) orders);
    }
}
