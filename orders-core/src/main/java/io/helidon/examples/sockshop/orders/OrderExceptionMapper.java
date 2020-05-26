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

import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_ACCEPTABLE;

/**
 * Exception mapper for {@code OrderException}s.
 */
@ApplicationScoped
@Provider
public class OrderExceptionMapper
        implements ExceptionMapper<OrderException> {
    @Override
    public Response toResponse(OrderException exception)
    {
        return Response
                .status(NOT_ACCEPTABLE)
                .entity(Collections.singletonMap("message", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}