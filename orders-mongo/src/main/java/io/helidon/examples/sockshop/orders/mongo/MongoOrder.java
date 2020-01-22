package io.helidon.examples.sockshop.orders.mongo;

import java.util.Collection;
import java.util.Date;

import io.helidon.examples.sockshop.orders.Address;
import io.helidon.examples.sockshop.orders.Card;
import io.helidon.examples.sockshop.orders.Customer;
import io.helidon.examples.sockshop.orders.Item;
import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.Shipment;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 * @author Aleksandar Seovic  2020.01.16
 */
public class MongoOrder extends Order {
    @BsonId
    public ObjectId _id;

    public MongoOrder() {
    }

    public MongoOrder(Order order) {
        super(order.getId(),
              order.getCustomerId(),
              order.getCustomer(),
              order.getAddress(),
              order.getCard(),
              order.getItems(),
              order.getShipment(),
              order.getDate(),
              order.getTotal());
    }

    public MongoOrder(String id,
                      String customerId,
                      Customer customer,
                      Address address,
                      Card card,
                      Collection<Item> items,
                      Shipment shipment, Date date, float total) {
        super(id, customerId, customer, address, card, items, shipment, date, total);
    }
}
