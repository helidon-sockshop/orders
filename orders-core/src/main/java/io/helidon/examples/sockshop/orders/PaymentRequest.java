package io.helidon.examples.sockshop.orders;

import java.util.UUID;

public class PaymentRequest {
    private String orderId;
    private Address address;
    private Card card;
    private Customer customer;
    private float amount;

    public PaymentRequest() {
    }

    public PaymentRequest(String orderId, Address address, Card card, Customer customer, float amount) {
        this.orderId = orderId;
        this.address = address;
        this.customer = customer;
        this.card = card;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "orderId=" + orderId +
                ", address=" + address +
                ", card=" + card +
                ", customer=" + customer +
                '}';
    }

    public String getOrderId() {
        if (orderId == null) {
            orderId = UUID.randomUUID().toString();
        }
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
