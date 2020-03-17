package io.helidon.examples.sockshop.orders;

import javax.enterprise.context.ApplicationScoped;

import static io.helidon.examples.sockshop.orders.TestDataFactory.payment;

@Mock
@ApplicationScoped
public class TestPaymentClient implements PaymentClient {
   public TestPaymentClient() {
   }

   public Payment pay(PaymentRequest request) {
      return payment(request.getCustomer().getId());
   }
}
