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

import static com.mongodb.client.model.Filters.eq;

/**
 * @author Aleksandar Seovic  2020.01.16
 */
@ApplicationScoped
@Specializes
public class MongoOrderRepository extends DefaultOrderRepository {
    private static final Logger LOGGER = Logger.getLogger(MongoOrderRepository.class.getName());

    @Inject
    private MongoCollection<MongoOrder> orders;

    @Override
    public MongoOrder getOrCreate(String orderId) {
        MongoOrder order = orders.find(eq("id", orderId)).first();
        if (order == null) {
            order = new MongoOrder();
        }
        return order;
    }

    @Override
    public Collection<? extends Order> findOrdersByCustomer(String customerId) {
        ArrayList<MongoOrder> results = new ArrayList<>();

        orders.find(eq("customerId", customerId))
                .forEach((Consumer<? super MongoOrder>) results::add);

        return results;
    }

    @Override
    public void saveOrder(Order order) {
        orders.insertOne(new MongoOrder(order));
    }
}
