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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public interface OrderApi {
    @GET
    @Path("search/customerId")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the orders for the specified customer")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "if orders exist"),
        @APIResponse(responseCode = "404", description = "if orders do not exist")
    })
    Response getOrdersForCustomer(@Parameter(description = "Customer identifier")
                                  @QueryParam("custId") String customerId);

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Return the order for the specified order")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "if the order exist"),
        @APIResponse(responseCode = "404", description = "if the order doesn't exist")
    })
    Response getOrder(@Parameter(description = "Order identifier")
                      @PathParam("id") String orderId);

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Place a new order for the specified order request")
    @APIResponses({
          @APIResponse(responseCode = "201", description = "if the request is successfully processed"),
          @APIResponse(responseCode = "406", description = "if the payment is not authorized")
    })
    Response newOrder(@Context UriInfo uriInfo,
                      @Parameter(description = "Order request") NewOrderRequest request);
}
