package io.helidon.examples.sockshop.orders;

import java.net.URI;

public class NewOrderRequest {

    public URI customer;
    public URI address;
    public URI card;
    public URI items;
}