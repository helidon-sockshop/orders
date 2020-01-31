package io.helidon.examples.sockshop.orders;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;

/**
 * Representation of a single order item.
 */
@Data
@NoArgsConstructor
@Entity
@IdClass(ItemId.class)
public class Item implements Serializable {
    /**
     * The item identifier.
     */
    @Id
    private String itemId;

    /**
     * The item quantity.
     */
    private int quantity;

    /**
     * The item's price per unit.
     */
    private float unitPrice;

    /**
     * The order this item belongs to, for JPA optimization.
     */
    @Id
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonbTransient
    @BsonIgnore
    private Order order;

    @Builder
    Item(String itemId, int quantity, float unitPrice) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
