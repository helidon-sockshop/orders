package io.helidon.examples.sockshop.orders;

import java.io.Serializable;

import lombok.Data;

/**
 * Composite JPA key for the {@link Item} class.
 */
@Data
public class ItemId implements Serializable {
    /**
     * The item identifier.
     */
    private String itemId;

    /**
     * The ID of the order this item belongs to.
     */
    private String order;
}
