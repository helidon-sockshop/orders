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
