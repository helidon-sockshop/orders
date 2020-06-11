/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shipping request that is sent to Shipping service for processing.
 */
@Data
@Builder
public class ShippingRequest implements Serializable {
    /**
     * Order identifier.
     */
    private String orderId;

    /**
     * Customer information.
     */
    private Customer customer;

    /**
     * Shipping address.
     */
    private Address address;

    /**
     * The number of items in the order.
     */
    private int itemCount;
}
