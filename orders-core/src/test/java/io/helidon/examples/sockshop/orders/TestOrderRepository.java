package io.helidon.examples.sockshop.orders;

// Tests need to interact with the entity backing OrderRepository
// to reset to initial state before every test
public interface TestOrderRepository {
    public void clear();
}
