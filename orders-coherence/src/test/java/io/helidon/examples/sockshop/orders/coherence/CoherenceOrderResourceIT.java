/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.coherence;

import java.net.URI;
import java.time.LocalDate;

import io.helidon.examples.sockshop.orders.NewOrderRequest;
import io.helidon.examples.sockshop.orders.Order;
import io.helidon.examples.sockshop.orders.OrderResourceIT;

import com.oracle.bedrock.testsupport.deferred.Eventually;

import com.tangosol.net.DefaultCacheServer;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static io.helidon.examples.sockshop.orders.Order.Status.PAYMENT_FAILED;
import static io.helidon.examples.sockshop.orders.Order.Status.SHIPPED;

import static io.restassured.RestAssured.given;

import static javax.ws.rs.core.Response.Status.CREATED;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.orders.OrderResource},
 * using Coherence for persistence.
 */
public class CoherenceOrderResourceIT extends OrderResourceIT {

    /**
     * Stop the server, as we cannot have multiple servers started at the same time.
     */
    @AfterAll
    static void stopServer() {
        SERVER.stop();
    }

    // We need to override the following test methods to take into account the
    // asynchronous nature of order processing when EventDrivenOrderProcessor
    // is used

    @Test
    protected void testCreateOrder() {
        String baseUri = "http://localhost:" + SERVER.port();
        NewOrderRequest req = NewOrderRequest.builder()
                .customer(URI.create(baseUri + "/customers/homer"))
                .address(URI.create(baseUri + "/addresses/homer:1"))
                .card(URI.create(baseUri + "/cards/homer:1234"))
                .items(URI.create(baseUri + "/carts/homer/items"))
                .build();

        given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/orders").
        then().
                statusCode(CREATED.getStatusCode()).
                body("total", is(14.0f),
                     "status", is("CREATED"));

        final String orderId = orders.getLastOrderId();
        Eventually.assertDeferred(() -> orders.get(orderId).getStatus(), is(SHIPPED));

        Order order = orders.get(orderId);
        assertThat(order.getPayment().isAuthorised(), is(true));
        assertThat(order.getShipment().getCarrier(), is("UPS"));
        assertThat(order.getShipment().getDeliveryDate(), is(LocalDate.now().plusDays(2)));
    }

    @Test
    protected void testPaymentFailure() {
        String baseUri = "http://localhost:" + SERVER.port();
        NewOrderRequest req = NewOrderRequest.builder()
                .customer(URI.create(baseUri + "/customers/lisa"))
                .address(URI.create(baseUri + "/addresses/lisa:1"))
                .card(URI.create(baseUri + "/cards/lisa:1234"))
                .items(URI.create(baseUri + "/carts/lisa/items"))
                .build();

        given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/orders").
        then().
                statusCode(CREATED.getStatusCode()).
                body("total", is(14.0f),
                    "status", is("CREATED"));

        final String orderId = orders.getLastOrderId();
        Eventually.assertDeferred(() -> orders.get(orderId).getStatus(), is(PAYMENT_FAILED));

        Order order = orders.get(orderId);
        assertThat(order.getPayment().isAuthorised(), is(false));
        assertThat(order.getPayment().getMessage(), is("Unable to parse authorization packet"));
    }

    @Test
    protected void testPaymentDeclined() {
        String baseUri = "http://localhost:" + SERVER.port();
        NewOrderRequest req = NewOrderRequest.builder()
                .customer(URI.create(baseUri + "/customers/bart"))
                .address(URI.create(baseUri + "/addresses/bart:1"))
                .card(URI.create(baseUri + "/cards/bart:1234"))
                .items(URI.create(baseUri + "/carts/bart/items"))
                .build();

        given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/orders").
        then().
                statusCode(CREATED.getStatusCode()).
                body("total", is(14.0f),
                    "status", is("CREATED"));

        final String orderId = orders.getLastOrderId();
        Eventually.assertDeferred(() -> orders.get(orderId).getStatus(), is(PAYMENT_FAILED));

        Order order = orders.get(orderId);
        assertThat(order.getPayment().isAuthorised(), is(false));
        assertThat(order.getPayment().getMessage(), is("Minors need parent approval"));
    }
}