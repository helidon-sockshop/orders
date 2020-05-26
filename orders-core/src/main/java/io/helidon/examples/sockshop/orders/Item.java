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
import org.eclipse.microprofile.openapi.annotations.media.Schema;

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
    @Schema(description = "The item identifier")
    private String itemId;

    /**
     * The item quantity.
     */
    @Schema(description = "The item quantity")
    private int quantity;

    /**
     * The item's price per unit.
     */
    @Schema(description = "The item's price per unit")
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
