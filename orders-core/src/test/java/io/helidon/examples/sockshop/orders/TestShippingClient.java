package io.helidon.examples.sockshop.orders;

import javax.enterprise.context.ApplicationScoped;

import static io.helidon.examples.sockshop.orders.TestDataFactory.shipment;

@Mock
@ApplicationScoped
public class TestShippingClient implements ShippingClient {
   public TestShippingClient() {
   }

   public Shipment ship(ShippingRequest request) {
      return shipment(request.getCustomer().getId());
   }
}
