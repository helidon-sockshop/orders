package io.helidon.examples.sockshop.orders.jpa;

import java.util.Collection;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderRepository;

import org.eclipse.microprofile.opentracing.Traced;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses relational database (via JPA) as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(APPLICATION)
@Traced
public class JpaOrderRepository implements OrderRepository {

    @PersistenceContext
    protected EntityManager em;

    @Override
    @Transactional
    public Collection<? extends Order> findOrdersByCustomer(String customerId) {
        String jql = "select o from Order as o where o.customer.id = :customerId order by o.date";
        TypedQuery<Order> query = em.createQuery(jql, Order.class);
        query.setParameter("customerId", customerId);

        return query.getResultList();
    }

    @Override
    @Transactional
    public Order get(String orderId) {
        return em.find(Order.class, orderId);
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        em.persist(order);
    }
}
