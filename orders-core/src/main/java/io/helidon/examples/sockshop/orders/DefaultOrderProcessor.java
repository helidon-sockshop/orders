/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.orders;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.helidon.microprofile.grpc.client.GrpcChannel;
import io.helidon.microprofile.grpc.client.GrpcServiceProxy;

import lombok.extern.java.Log;

import static io.helidon.examples.sockshop.orders.Order.Status.PAID;
import static io.helidon.examples.sockshop.orders.Order.Status.PAYMENT_FAILED;

/**
 * Default implementation of the {@link OrderProcessor} service.
 * <p>
 * Note that this implementation is quite naive and attempts to
 * process payment and submit order for shipping synchronously.
 * If either fails, for whatever reason, the order will not be captured
 * and the error will be returned to the caller.
 * <p>
 * More realistic implementation would store the submitted order
 * immediately, and use some kind of reactive/event driven/messaging
 * implementation to subsequently process payment and submit order for
 * shipping, allowing for manual intervention in case anything goes wrong.
 * <p>
 * However, for both {@code core} and most back end implementations
 * that would require that we introduce some kind of messaging layer,
 * which would complicate the architecture quite a bit.
 * <p>
 * For an example of a more resilient, event driven implementation,
 * check out {@code orders-coherence} module.
 */
@Log
@ApplicationScoped
public class DefaultOrderProcessor implements OrderProcessor {
    /**
     * Order repository to use.
     */
    @Inject
    protected OrderRepository orders;

    /**
     * Shipping service client.
     */
    @Inject
    @GrpcServiceProxy
    protected ShippingClient shippingService;

    /**
     * Payment service client.
     */
    @Inject
    @GrpcServiceProxy
    protected PaymentClient paymentService;

    // --- OrderProcessor interface -----------------------------------------

    @Override
    public void processOrder(Order order) {
        processPayment(order);
        shipOrder(order);

        orders.saveOrder(order);
    }

    // ---- helpers ---------------------------------------------------------

    /**
     * Process payment and update order with payment details.
     *
     * @param order the order to process the payment for
     *
     * @throws PaymentDeclinedException if the payment was declined
     */
    protected void processPayment(Order order) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                        .orderId(order.getOrderId())
                        .customer(order.getCustomer())
                        .address(order.getAddress())
                        .card(order.getCard())
                        .amount(order.getTotal())
                        .build();

        log.info("Processing Payment: " + paymentRequest);
        Payment payment = paymentService.authorize(paymentRequest);
        log.info("Payment processed: " + payment);

        order.setPayment(payment);
        if (payment == null) {
            order.setStatus(PAYMENT_FAILED);
            throw new PaymentDeclinedException("Unable to parse authorization packet");
        }
        if (!payment.isAuthorised()) {
            order.setStatus(PAYMENT_FAILED);
            throw new PaymentDeclinedException(payment.getMessage());
        }

        order.setStatus(PAID);
    }

    /**
     * Submits order for shipping and updates order with shipment details.
     *
     * @param order the order to ship
     */
    protected void shipOrder(Order order) {
        ShippingRequest shippingRequest = ShippingRequest.builder()
                        .orderId(order.getOrderId())
                        .customer(order.getCustomer())
                        .address(order.getAddress())
                        .itemCount(order.getItems().size())
                        .build();

        log.info("Creating Shipment: " + shippingRequest);
        Shipment shipment = shippingService.ship(shippingRequest);
        log.info("Created Shipment: " + shipment);

        order.setShipment(shipment);
    }

    // ---- helper methods --------------------------------------------------

    /**
     * An exception that is thrown if the payment is declined.
     */
    public static class PaymentDeclinedException extends OrderException {
        public PaymentDeclinedException(String s) {
            super(s);
        }
    }
}
