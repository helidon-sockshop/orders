package io.helidon.examples.sockshop.orders;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * CDI producer for a default HTTP client.
 */
@ApplicationScoped
public class DefaultClientProducer {
    /**
     * Create a default HTTP client.
     *
     * @return a default HTTP client
     */
    @Produces
    Client client() {
        return ClientBuilder.newClient();
    }
}
