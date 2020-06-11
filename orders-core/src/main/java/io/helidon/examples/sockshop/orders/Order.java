/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Order information.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order implements Serializable, Comparable<Order> {
    /**
     * Order identifier.
     */
    @Id
    @JsonbProperty("id")
    @Schema(description = "Order identifier")
    private String orderId;

    /**
     * Customer information.
     */
    @Embedded
    @Schema(description = "Customer information")
    private Customer customer;

    /**
     * Billing/shipping address.
     */
    @Embedded
    @Schema(description = "Billing/shipping address")
    private Address address;

    /**
     * Payment card details.
     */
    @Embedded
    @Schema(description = "Payment card details")
    private Card card;

    /**
     * Order date and time.
     */
    @Schema(description = "Order date and time")
    private LocalDateTime date;

    /**
     * Order total.
     */
    @Schema(description = "Order total")
    private float total;

    /**
     * Order items.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Order items")
    private Collection<Item> items = new ArrayList<>();

    /**
     * Payment authorization.
     */
    @Embedded
    @Schema(description = "Payment authorization")
    private Payment payment;

    /**
     * Shipment details.
     */
    @Embedded
    @Schema(description = "Shipment details")
    private Shipment shipment;

    /**
     * Order status.
     */
    @Schema(description = "Order status")
    private Status status;

    @Builder
    public Order(Customer customer,
                 Address address,
                 Card card,
                 Collection<Item> items) {
        this.orderId  = UUID.randomUUID().toString().substring(0, 8);
        this.date     = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.customer = customer;
        this.address  = address;
        this.card     = card;
        this.status   = Status.CREATED;
        this.setItems(items);
    }

    /**
     * Set order items.
     *
     * @param items order items
     */
    public void setItems(Collection<Item> items) {
        float total = 0.0f;
        for (Item item : items) {
            total += item.getQuantity() * item.getUnitPrice();
            item.setOrder(this);
        }
        this.total = total;
        this.items = items;
    }

    /**
     * Order links.
     *
     * @return order links
     */
    @JsonbProperty("_links")
    public Links getLinks() {
        return Links.order(orderId);
    }

    @Override
    public int compareTo(Order o) {
        return date.compareTo(o.date) * -1;
    }

    /**
     * Order status enumeration.
     */
    public enum Status {
        /**
         * Order was created.
         */
        CREATED,

        /**
         * Payment was processed successfully.
         */
        PAID,

        /**
         * Order was shipped.
         */
        SHIPPED,

        /**
         * Payment was not authorized.
         */
        PAYMENT_FAILED,

        /**
         * Order shipment failed.
         */
        SHIPMENT_FAILED
    }
}
