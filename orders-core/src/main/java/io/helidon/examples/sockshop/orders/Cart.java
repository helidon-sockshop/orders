package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public class Cart implements Serializable {
    @NotNull
    private String customerId;

    private String id;

    private List<Item> items = new ArrayList<>();

    public Cart(String customerId) {
        this.customerId = customerId;
    }

    public Cart() {
        this(null);
    }

    public List<Item> contents() {
        return items;
    }

    public Cart add(Item item) {
        items.add(item);
        return this;
    }

    public Cart remove(Item item) {
        items.remove(item);
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", items=" + items +
                '}';
    }
}
