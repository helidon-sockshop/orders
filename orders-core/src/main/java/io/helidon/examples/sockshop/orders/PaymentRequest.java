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

import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Payment request that is sent to Payment service for authorization.
 */
@Data
@Builder
public class PaymentRequest {
    /**
     * Order identifier.
     */
    @Schema(description = "Order identifier")
    private String orderId;

    /**
     * Customer information.
     */
    @Schema(description = "Customer information")
    private Customer customer;

    /**
     * Billing address.
     */
    @Schema(description = "Billing address")
    private Address address;

    /**
     * Payment card details.
     */
    @Schema(description = "Payment card details")
    private Card card;

    /**
     * Payment amount.
     */
    @Schema(description = "Payment amount")
    private float amount;
}
