package io.helidon.examples.sockshop.orders;

/**
 * Tests for default in memory repository implementation.
 */
public class DefaultOrderRepositoryTest extends OrderRepositoryTest {
    @Override
    protected TestOrderRepository getOrderRepository() {
        return new TestDefaultOrderRepository();
    }
}
