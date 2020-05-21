package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

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
    private Collection<Item> items;

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

    @Builder
    public Order(String orderId,
                 Customer customer,
                 Address address,
                 Card card,
                 LocalDateTime date,
                 float total,
                 Collection<Item> items,
                 Payment payment,
                 Shipment shipment) {
        this.orderId = orderId;
        this.customer = customer;
        this.address = address;
        this.card = card;
        this.date = date;
        this.total = total;
        this.items = items;
        this.payment = payment;
        this.shipment = shipment;
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
}
