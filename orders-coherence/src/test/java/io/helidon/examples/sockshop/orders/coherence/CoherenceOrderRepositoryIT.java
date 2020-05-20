package io.helidon.examples.sockshop.orders.coherence;

import io.helidon.examples.sockshop.orders.OrderRepositoryTest;
import io.helidon.examples.sockshop.orders.TestOrderRepository;

import com.tangosol.net.CacheFactory;

/**
 * Tests for Coherence repository implementation.
 */
class CoherenceOrderRepositoryIT extends OrderRepositoryTest {
    @Override
    public TestOrderRepository getOrderRepository() {
        return new TestCoherenceOrderRepository(CacheFactory.getCache("orders"));
    }
}