package io.helidon.examples.sockshop.orders;

import java.util.concurrent.CompletionStage;

// Tests need to interact with the entity backing OrderRepository
// to reset to initial state before every test
public interface ClearOrderRepository {
    public CompletionStage clear();
}
