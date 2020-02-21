package io.helidon.examples.sockshop.orders;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

@Alternative
@Priority(Interceptor.Priority.APPLICATION+10)
public class TestDefaultOrderRepository extends DefaultOrderRepository implements TestOrderRepository {
    public TestDefaultOrderRepository() {}

    @Override
    public void clear() {
        orders.clear();
    }
}
