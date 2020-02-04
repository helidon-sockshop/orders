package io.helidon.examples.sockshop.orders.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import io.helidon.examples.sockshop.orders.DefaultOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import com.mongodb.client.MongoCollection;
import org.bson.BsonDocument;
import org.eclipse.microprofile.opentracing.Traced;

import static com.mongodb.client.model.Filters.eq;

/**
 * An implementation of {@link io.helidon.examples.sockshop.orders.OrderRepository}
 * that that uses MongoDB as a backend data store.
 */
@ApplicationScoped
@Specializes
@Traced
public class MongoOrderRepository extends DefaultOrderRepository {

    private MongoCollection<Order> orders;

    @Inject
    MongoOrderRepository(MongoCollection<Order> orders) {
        this.orders = orders;
    }

    @Override
    public Order get(String orderId) {
        return orders.find(eq("orderId", orderId)).first();
    }

    @Override
    public Collection<? extends Order> findOrdersByCustomer(String customerId) {
        ArrayList<Order> results = new ArrayList<>();

        orders.find(eq("customer._id", customerId))
                .forEach((Consumer<? super Order>) results::add);

        return results;
    }

    @Override
    public void saveOrder(Order order) {
        orders.insertOne(order);
    }

    // ---- helpers ---------------------------------------------------------

    @Override
    public void clear() {
        orders.deleteMany(new BsonDocument());
    }
}
