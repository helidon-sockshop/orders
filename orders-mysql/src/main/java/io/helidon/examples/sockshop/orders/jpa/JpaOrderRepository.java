package io.helidon.examples.sockshop.orders.jpa;

import java.util.Collection;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;

import java.util.function.Supplier;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import io.helidon.common.configurable.ThreadPoolSupplier;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderRepository;

import org.eclipse.microprofile.opentracing.Traced;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses relational database (via JPA) as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION+10)
@Traced
public class JpaOrderRepository implements OrderRepository {
    @ApplicationScoped
    @Alternative
    @Priority(Interceptor.Priority.APPLICATION+10)
    @Traced
    protected static class SyncOrderRepository {
       @Inject
       public SyncOrderRepository() {}

       @PersistenceContext
       protected EntityManager em;

       @Transactional
       public Collection<? extends Order> findOrdersByCustomerSync(String customerId) {
          String jql = "select o from Order as o where o.customer.id = :customerId order by o.date";
          TypedQuery<Order> query = em.createQuery(jql, Order.class);
          query.setParameter("customerId", customerId);

          return query.getResultList();
       }

       @Transactional
       public Order getSync(String orderId) {
          return em.find(Order.class, orderId);
       }

       @Transactional
       public boolean saveOrderSync(Order order) {
          em.persist(order);
          return true;
       }
    }

    @Inject
    protected SyncOrderRepository repo;

    protected static final Supplier<ExecutorService> ses = ThreadPoolSupplier.builder()
                                          .threadNamePrefix("jpa-em-")
                                          .build();

    // TODO: make it configurable
    protected static final ExecutorService es = ses.get();

    @Override
    public CompletionStage<Collection<? extends Order>> findOrdersByCustomer(String customerId) {
        return CompletableFuture.supplyAsync(() -> repo.findOrdersByCustomerSync(customerId), es);
    }

    @Override
    public CompletionStage<Order> get(String orderId) {
        return CompletableFuture.supplyAsync(() -> repo.getSync(orderId), es);
    }

    @Override
    public CompletionStage saveOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> repo.saveOrderSync(order), es);
    }

}
