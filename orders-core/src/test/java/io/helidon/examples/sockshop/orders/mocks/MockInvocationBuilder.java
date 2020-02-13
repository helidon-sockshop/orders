package io.helidon.examples.sockshop.orders.mocks;

import java.net.URI;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.RxInvoker;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import io.helidon.examples.sockshop.orders.Address;
import io.helidon.examples.sockshop.orders.Card;
import io.helidon.examples.sockshop.orders.Customer;
import io.helidon.examples.sockshop.orders.Payment;
import io.helidon.examples.sockshop.orders.PaymentRequest;
import io.helidon.examples.sockshop.orders.Shipment;
import io.helidon.examples.sockshop.orders.ShippingRequest;

import static io.helidon.examples.sockshop.orders.TestDataFactory.*;

/**
 * Mock Invocation.Builder, where most of the actual work happens.
 */
@SuppressWarnings("unchecked")
public class MockInvocationBuilder implements Invocation.Builder {
    private URI uri;

    MockInvocationBuilder(URI uri) {
        this.uri = uri;
    }

    // the methods we care about...
    @Override
    public <T> T get(Class<T> c) {
        if (c.isAssignableFrom(Customer.class)) {
            String id = uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1);
            return (T) customer(id);
        }
        if (c.isAssignableFrom(Address.class)) {
            return (T) address();
        }
        if (c.isAssignableFrom(Card.class)) {
            return (T) card();
        }
        return null;
    }

    @Override
    public <T> T get(GenericType<T> genericType) {
        // this is only called for List<Item>
        return (T) items(3);
    }

    @Override
    public <T> T post(Entity<?> entity, Class<T> c) {
        if (c.isAssignableFrom(Payment.class)) {
            return (T) payment(((PaymentRequest) entity.getEntity()).getCustomer().getId());
        }
        if (c.isAssignableFrom(Shipment.class)) {
            return (T) shipment(((ShippingRequest) entity.getEntity()).getCustomer().getId());
        }

        return null;
    }

    // -- everything else can remain unimplemented

    @Override
    public Invocation build(String s) {
        return null;
    }

    @Override
    public Invocation build(String s, Entity<?> entity) {
        return null;
    }

    @Override
    public Invocation buildGet() {
        return null;
    }

    @Override
    public Invocation buildDelete() {
        return null;
    }

    @Override
    public Invocation buildPost(Entity<?> entity) {
        return null;
    }

    @Override
    public Invocation buildPut(Entity<?> entity) {
        return null;
    }

    @Override
    public AsyncInvoker async() {
        return new RInvoker();
    }

    @Override
    public Invocation.Builder accept(String... strings) {
        return null;
    }

    @Override
    public Invocation.Builder accept(MediaType... mediaTypes) {
        return null;
    }

    @Override
    public Invocation.Builder acceptLanguage(Locale... locales) {
        return null;
    }

    @Override
    public Invocation.Builder acceptLanguage(String... strings) {
        return null;
    }

    @Override
    public Invocation.Builder acceptEncoding(String... strings) {
        return null;
    }

    @Override
    public Invocation.Builder cookie(Cookie cookie) {
        return null;
    }

    @Override
    public Invocation.Builder cookie(String s, String s1) {
        return null;
    }

    @Override
    public Invocation.Builder cacheControl(CacheControl cacheControl) {
        return null;
    }

    @Override
    public Invocation.Builder header(String s, Object o) {
        return null;
    }

    @Override
    public Invocation.Builder headers(MultivaluedMap<String, Object> multivaluedMap) {
        return null;
    }

    @Override
    public Invocation.Builder property(String s, Object o) {
        return null;
    }

    @Override
    public CompletionStageRxInvoker rx() {
        return new RInvoker();
    }

    @Override
    public <T extends RxInvoker> T rx(Class<T> aClass) {
        return null;
    }

    @Override
    public Response get() {
        return null;
    }

    @Override
    public Response put(Entity<?> entity) {
        return null;
    }

    @Override
    public <T> T put(Entity<?> entity, Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T put(Entity<?> entity, GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response post(Entity<?> entity) {
        return null;
    }

    @Override
    public <T> T post(Entity<?> entity, GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response delete() {
        return null;
    }

    @Override
    public <T> T delete(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T delete(GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response head() {
        return null;
    }

    @Override
    public Response options() {
        return null;
    }

    @Override
    public <T> T options(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T options(GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response trace() {
        return null;
    }

    @Override
    public <T> T trace(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T trace(GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response method(String s) {
        return null;
    }

    @Override
    public <T> T method(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T method(String s, GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response method(String s, Entity<?> entity) {
        return null;
    }

    @Override
    public <T> T method(String s, Entity<?> entity, Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T method(String s, Entity<?> entity, GenericType<T> genericType) {
        return null;
    }


    protected class RInvoker implements CompletionStageRxInvoker, AsyncInvoker {
        @Override
        public CompletableFuture<Response> get() {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.get());
        }

        @Override
        public <T> CompletableFuture<T> get(Class<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.get(responseType));
        }

        @Override
        public <T> CompletableFuture<T> get(GenericType<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.get(responseType));
        }

        public <T> CompletableFuture<T> get(InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }

        @Override
        public CompletableFuture<Response> put(Entity<?> entity) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.put(entity));
        }

        @Override
        public <T> CompletableFuture<T> put(Entity<?> entity, Class<T> clazz) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.put(entity, clazz));
        }

        @Override
        public <T> CompletableFuture<T> put(Entity<?> entity, GenericType<T> type) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.put(entity, type));
        }

        public <T> CompletableFuture<T> put(Entity<?> entity, InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }

        @Override
        public CompletableFuture<Response> post(Entity<?> entity) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.post(entity));
        }

        @Override
        public <T> CompletableFuture<T> post(Entity<?> entity, Class<T> clazz) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.post(entity, clazz));
        }

        @Override
        public <T> CompletableFuture<T> post(Entity<?> entity, GenericType<T> type) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.post(entity, type));
        }

        public <T> CompletableFuture<T> post(Entity<?> entity, InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }

        @Override
        public CompletableFuture<Response> delete() {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.delete());
        }

        @Override
        public <T> CompletableFuture<T> delete(Class<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.delete(responseType));
        }

        @Override
        public <T> CompletableFuture<T> delete(GenericType<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.delete(responseType));
        }

        public <T> CompletableFuture<T> delete(InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }

        @Override
        public CompletableFuture<Response> head() {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.head());
        }

        public CompletableFuture<Response> head(InvocationCallback<Response> callback) {
            return head().thenApply(r -> { callback.completed(r); return r; })
                         .exceptionally(th -> { callback.failed(th); return null; });
        }

        @Override
        public CompletableFuture<Response> options() {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.options());
        }

        @Override
        public <T> CompletableFuture<T> options(Class<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.options(responseType));
        }

        @Override
        public <T> CompletableFuture<T> options(GenericType<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.options(responseType));
        }

        public <T> CompletableFuture<T> options(InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }

        @Override
        public CompletableFuture<Response> trace() {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.trace());
        }

        @Override
        public <T> CompletableFuture<T> trace(Class<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.trace(responseType));
        }

        @Override
        public <T> CompletableFuture<T> trace(GenericType<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.trace(responseType));
        }

        public <T> CompletableFuture<T> trace(InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }

        @Override
        public CompletableFuture<Response> method(String name) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.method(name));
        }

        @Override
        public <T> CompletableFuture<T> method(String name, Class<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.method(name, responseType));
        }

        @Override
        public <T> CompletableFuture<T> method(String name, GenericType<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.method(name, responseType));
        }

        @Override
        public CompletableFuture<Response> method(String name, Entity<?> entity) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.method(name, entity));
        }

        @Override
        public <T> CompletableFuture<T> method(String name, Entity<?> entity, Class<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.method(name, entity, responseType));
        }

        @Override
        public <T> CompletableFuture<T> method(String name, Entity<?> entity, GenericType<T> responseType) {
            return CompletableFuture.completedFuture(MockInvocationBuilder.this.method(name, entity, responseType));
        }

        public <T> CompletableFuture<T> method(String name, InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }

        public <T> CompletableFuture<T> method(String name, Entity<?> entity, InvocationCallback<T> callback) {
            return CompletableFuture.<T>failedFuture(new Exception("Mock Client does not implement these"))
                                    .thenApply(r -> { callback.completed(r); return r; })
                                    .exceptionally(th -> { callback.failed(th); return null; });
        }
    }
}
