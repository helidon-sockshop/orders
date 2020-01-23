package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.util.Objects;

public class Shipment implements Serializable {
    private String orderId;
    private String trackingNumber;

    public Shipment() {
    }

    public Shipment(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shipment shipment = (Shipment) o;
        return orderId.equals(shipment.orderId) &&
                trackingNumber.equals(shipment.trackingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, trackingNumber);
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id='" + orderId + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                '}';
    }
}
