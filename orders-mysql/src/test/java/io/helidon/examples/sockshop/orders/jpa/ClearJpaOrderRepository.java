package io.helidon.examples.sockshop.orders.jpa;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
import javax.transaction.Transactional;

import io.helidon.examples.sockshop.orders.ClearOrderRepository;

@Alternative
@Priority(Interceptor.Priority.APPLICATION+100)
public class ClearJpaOrderRepository extends JpaOrderRepository implements ClearOrderRepository {
    @Alternative
    @Priority(Interceptor.Priority.APPLICATION+100)
    protected static class ClearSyncOrderRepository extends SyncOrderRepository {
       @Transactional
       public boolean clearSync() {
          em.createQuery("delete from Item").executeUpdate();
          em.createQuery("delete from Order").executeUpdate();
          return true;
       }
    }

    @Override
    public CompletionStage clear() {
        return CompletableFuture.supplyAsync(() -> ((ClearSyncOrderRepository)repo).clearSync(), es);
    }

    public static CompletionStage clearRepository(ClearJpaOrderRepository cor) {
        return cor.clear();
    }
}
