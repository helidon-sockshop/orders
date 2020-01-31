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
     * Billing address.
     */
    private Address address;

    /**
     * The number of items in the order.
     */
    private int itemCount;
}
