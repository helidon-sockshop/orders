package io.helidon.examples.sockshop.orders;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.helidon.common.CollectionsHelper;

/**
 * Order Service HTTP application.
 * <p/>
 * This is the entry point for this service, and it is necessary in order
 * to configure a custom exception mapper in addition to the resources
 * implementing REST API.
 */
@ApplicationScoped
@ApplicationPath("/")
public class OrderService extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return CollectionsHelper.setOf(
                OrderResource.class,
                OrderExceptionMapper.class);
    }
}