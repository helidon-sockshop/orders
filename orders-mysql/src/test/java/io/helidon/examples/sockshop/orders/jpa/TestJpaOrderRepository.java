package io.helidon.examples.sockshop.orders.jpa;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
import javax.transaction.Transactional;

import io.helidon.examples.sockshop.orders.TestOrderRepository;

@Alternative
@Priority(Interceptor.Priority.APPLICATION+100)
public class TestJpaOrderRepository extends JpaOrderRepository implements TestOrderRepository {
    @Transactional
    public void clear() {
        em.createQuery("delete from Item").executeUpdate();
        em.createQuery("delete from Order").executeUpdate();
    }
}
