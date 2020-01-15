package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;
import javax.ws.rs.core.Link;

public class Order implements Serializable, Comparable<Order> {

    private String id;

    private String customerId;

    private Customer customer;

    private Address address;

    private Card card;

    private Collection<Item> items;

    private Shipment shipment;

    private Date date = Calendar.getInstance().getTime();

    private float total;

    private Map<String, Map<String, String>> links = new LinkedHashMap<>();

    public Order() {
    }

    public Order(String id, String customerId, Customer customer, Address address, Card card,
                 Collection<Item> items, Shipment shipment, Date date, float total) {
        this.id = id;
        this.customerId = customerId;
        this.customer = customer;
        this.address = address;
        this.card = card;
        this.items = items;
        this.shipment = shipment;
        this.date = date;
        this.total = total;
    }

    public void addLink(String name, Link link) {
        this.links.put(name, Collections.singletonMap("href", link.getUri().toString()));
    }

    @Override
    public int compareTo(Order o) {
        return date.compareTo(o.date) * -1;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customer=" + customer +
                ", address=" + address +
                ", card=" + card +
                ", items=" + items +
                ", date=" + date +
                '}';
    }

    // Crappy getter setters for Jackson

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Collection<Item> getItems() {
        return items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @JsonbProperty("_links")
    public Map<String, Map<String, String>> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Map<String, String>> links) {
        this.links = links;
    }
}
