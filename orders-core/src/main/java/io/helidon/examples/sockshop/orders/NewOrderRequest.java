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

import java.net.URI;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * The incoming request for new orders.
 */
@ToString
@NoArgsConstructor
public class NewOrderRequest {
    /**
     * The URI that should be used to fetch customer information.
     */
    @Schema(description = "The URI that should be used to fetch customer information")
    public URI customer;

    /**
     * The URI that should be used to fetch billing/shipping address information.
     */
    @Schema(description = "The URI that should be used to fetch billing/shipping address information")
    public URI address;

    /**
     * The URI that should be used to fetch payment card information.
     */
    @Schema(description = "The URI that should be used to fetch payment card information")
    public URI card;

    /**
     * The URI that should be used to fetch order items from the shopping cart.
     */
    @Schema(description = "The URI that should be used to fetch order items from the shopping cart")
    public URI items;

    @Builder
    NewOrderRequest(URI customer, URI address, URI card, URI items) {
        this.customer = customer;
        this.address = address;
        this.card = card;
        this.items = items;
    }
}