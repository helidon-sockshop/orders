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

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Shipping or billing address.
 */
@Data
@NoArgsConstructor
@Embeddable
public class Address implements Serializable {
    /**
     * Street number.
     */
    @Schema(description = "Street number")
    private String number;

    /**
     * Street name.
     */
    @Schema(description = "Street name")
    private String street;

    /**
     * City name.
     */
    @Schema(description = "City name")
    private String city;

    /**
     * Postal code.
     */
    @Schema(description = "Postal code")
    private String postcode;

    /**
     * Country name.
     */
    @Schema(description = "County name")
    private String country;

    @Builder
    Address(String number, String street, String city, String postcode, String country) {
        this.number = number;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
    }
}
