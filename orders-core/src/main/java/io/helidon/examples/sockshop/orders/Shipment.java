package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.util.Objects;

public class Shipment implements Serializable {
    private String id;
    private String trackingNumber;

    public Shipment() {
    }

    public Shipment(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return id.equals(shipment.id) &&
                trackingNumber.equals(shipment.trackingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trackingNumber);
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id='" + id + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                '}';
    }
}
