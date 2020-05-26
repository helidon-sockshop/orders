/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.orders;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://user/")
public interface UsersClient {
   @Path("/addresses/{addressId}")
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
