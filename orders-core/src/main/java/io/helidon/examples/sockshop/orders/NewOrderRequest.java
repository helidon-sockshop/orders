package io.helidon.examples.sockshop.orders;

import java.net.URI;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The incoming request for new orders.
 */
@ToString
@NoArgsConstructor
public class NewOrderRequest {
    /**
     * The URI that should be used to fetch customer information.
     */
    public String customer;

    /**
     * The URI that should be used to fetch billing/shipping address information.
     */
    public String address;

    /**
     * The URI that should be used to fetch payment card information.
     */
    public String card;

    /**
     * The URI that should be used to fetch order items from the shopping cart.
     */
    public String items;

    @Builder
    NewOrderRequest(String customer, String address, String card, String items) {
        this.customer = customer;
        this.address = address;
        this.card = card;
        this.items = items;
    }
}
