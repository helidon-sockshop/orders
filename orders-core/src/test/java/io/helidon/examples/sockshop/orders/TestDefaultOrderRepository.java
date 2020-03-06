package io.helidon.examples.sockshop.orders;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Alternative
@Priority(APPLICATION - 5)
public class TestDefaultOrderRepository extends DefaultOrderRepository implements TestOrderRepository {
    @Override
    public void clear() {
        orders.clear();
    }
}
