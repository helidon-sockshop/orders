package io.helidon.examples.sockshop.orders;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="carts-service")
public interface CartsClient {
   @Path("/carts/{cartId}/items")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   public List<Item> cart(@PathParam("cartId") String cartId);
}
