/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders;

/**
 * Tests for default in memory repository implementation.
 */
public class DefaultOrderRepositoryTest extends OrderRepositoryTest {
    @Override
    protected TestOrderRepository getOrderRepository() {
        return new TestDefaultOrderRepository();
    }
}
