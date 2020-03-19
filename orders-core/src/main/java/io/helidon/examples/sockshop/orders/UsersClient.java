package io.helidon.examples.sockshop.orders;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey="users-service")
public interface UsersClient {
   @Path("/address/{addressId}")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   public Address address(@PathParam("addressId") String addressId);

   @Path("/cards/{cardId}")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   public Card card(@PathParam("cardId") String cardId);

   @Path("/customers/{customerId}")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   public Customer customer(@PathParam("customerId") String customerId);
}
