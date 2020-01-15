package io.helidon.examples.sockshop.orders;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.helidon.common.CollectionsHelper;

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