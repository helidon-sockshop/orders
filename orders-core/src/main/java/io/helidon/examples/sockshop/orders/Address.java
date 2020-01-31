package io.helidon.examples.sockshop.orders;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String number;

    /**
     * Street name.
     */
    private String street;

    /**
     * City name.
     */
    private String city;

    /**
     * Postal code.
     */
    private String postcode;

    /**
     * Country name.
     */
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
