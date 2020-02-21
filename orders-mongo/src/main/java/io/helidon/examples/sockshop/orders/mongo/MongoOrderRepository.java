package io.helidon.examples.sockshop.orders.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderRepository;

import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.SingleResultCallback;
import org.eclipse.microprofile.opentracing.Traced;

import static com.mongodb.client.model.Filters.eq;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses MongoDB as a backend data store.
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION+10)
@Traced
public class MongoOrderRepository implements OrderRepository {
    protected MongoCollection<Order> orders;

    @Inject
    MongoOrderRepository(MongoCollection<Order> orders) {
        this.orders = orders;
    }

    @Override
    public CompletionStage<Order> get(String orderId) {
        CompletableFuture<Order> cf = new CompletableFuture<>();

        orders.find(eq("orderId", orderId))
              .first(complete(cf));
        return cf;
    }

    @Override
    public CompletionStage<Collection<? extends Order>> findOrdersByCustomer(String customerId) {
        ArrayList<Order> results = new ArrayList<>();

        CompletableFuture<ArrayList<Order>> cf = new CompletableFuture<>();
        orders.find(eq("customer._id", customerId))
              .into(results, complete(cf));

        return cf.thenApply(t -> t);
    }

    @Override
    public CompletionStage saveOrder(Order order) {
        CompletableFuture cf = new CompletableFuture();
        orders.insertOne(order, complete(cf));
        return cf;
    }

    // ---- helpers ---------------------------------------------------------

    protected static <T> SingleResultCallback<T> complete(CompletableFuture<T> cf) {
        return (r, th) -> {
                if (th == null) {
                    cf.complete(r);
                } else {
                    cf.completeExceptionally(th);
                }
            };
    }
}
