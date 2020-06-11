/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.json.bind.annotation.JsonbCreator;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shipment information received from the Shipping service.
 */
@Data
@NoArgsConstructor
@Embeddable
public class Shipment implements Serializable {
    /**
     * Shipping carrier.
     */
    private String carrier;

    /**
     * Tracking number.
     */
    private String trackingNumber;

    /**
     * Estimated delivery date.
     */
    private LocalDate deliveryDate;

    @Builder
    Shipment(String carrier, String trackingNumber, LocalDate deliveryDate) {
        this.carrier = carrier;
        this.trackingNumber = trackingNumber;
        this.deliveryDate = deliveryDate;
    }
}
