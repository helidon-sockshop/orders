/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders;

import javax.enterprise.context.ApplicationScoped;

import io.helidon.examples.sockshop.orders.TestDataFactory;

@Mock
@ApplicationScoped
public class TestUsersClient implements UsersClient {
   public TestUsersClient() {
   }

   public Address address(String addressId) {
      return TestDataFactory.address();
   }

   public Card card(String cardId) {
      return TestDataFactory.card();
   }

   public Customer customer(String customerId) {
      return TestDataFactory.customer(customerId);
   }
}
