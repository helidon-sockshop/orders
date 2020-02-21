package io.helidon.examples.sockshop.orders;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Specializes;

@Specializes
public class ClearDefaultOrderRepository extends DefaultOrderRepository implements ClearOrderRepository {
    public ClearDefaultOrderRepository() {}

    @Override
    public CompletionStage clear() {
        clearRepository(this);
        return CompletableFuture.completedStage(null);
    }

    public static void clearRepository(DefaultOrderRepository dor) {
        dor.orders.clear();
    }
}
