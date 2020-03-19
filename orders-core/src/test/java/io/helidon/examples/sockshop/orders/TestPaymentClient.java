package io.helidon.examples.sockshop.orders;

import javax.enterprise.context.ApplicationScoped;

import static io.helidon.examples.sockshop.orders.TestDataFactory.payment;

@Mock
@ApplicationScoped
public class TestPaymentClient implements PaymentClient {
   public TestPaymentClient() {
   }

   public Payment authorize(PaymentRequest request) {
      return payment(request.getCustomer().getId());
   }
}
