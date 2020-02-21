package io.helidon.examples.sockshop.orders.mongo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import io.helidon.examples.sockshop.orders.ClearOrderRepository;
import io.helidon.examples.sockshop.orders.Order;

import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.SingleResultCallback;
import org.bson.BsonDocument;

@Alternative
@Priority(Interceptor.Priority.APPLICATION+100)
public class ClearMongoOrderRepository extends MongoOrderRepository implements ClearOrderRepository {
    @Inject
    public ClearMongoOrderRepository(MongoCollection<Order> orders) {
        super(orders);
    }

    @Override
    public CompletionStage clear() {
        return clearRepository(this);
    }

    public static CompletionStage clearRepository(MongoOrderRepository mor) {
        CompletableFuture cf = new CompletableFuture();
        mor.orders.deleteMany(new BsonDocument(), complete(cf));
        return cf;
    }
}
