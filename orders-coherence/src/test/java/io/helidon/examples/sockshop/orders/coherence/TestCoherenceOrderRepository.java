package io.helidon.examples.sockshop.orders.coherence;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.TestOrderRepository;

import com.oracle.coherence.cdi.Cache;
import com.tangosol.net.NamedCache;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Alternative
@Priority(APPLICATION + 5)
public class TestCoherenceOrderRepository extends CoherenceOrderRepository
        implements TestOrderRepository {
    @Inject
    public TestCoherenceOrderRepository(@Cache("orders") NamedCache<String, Order> orders) {
        super(orders);
    }

    public void clear() {
        orders.clear();
    }
}
