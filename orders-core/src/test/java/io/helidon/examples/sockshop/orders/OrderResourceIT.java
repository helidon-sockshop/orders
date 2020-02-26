package io.helidon.examples.sockshop.orders;

import java.net.URI;
import java.time.LocalDate;

import javax.ws.rs.client.Client;

import io.helidon.microprofile.server.Server;

import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.jboss.weld.proxy.WeldClientProxy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.helidon.examples.sockshop.orders.TestDataFactory.order;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import static io.restassured.RestAssured.when;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_ACCEPTABLE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for {@link io.helidon.examples.sockshop.orders.OrderResource}.
 */
public class OrderResourceIT {
    /**
     * This will start the application on ephemeral port to avoid port conflicts.
     * We can discover the actual port by calling {@link io.helidon.microprofile.server.Server#port()} method afterwards.
     */
    public static final Server SERVER = Server.builder().port(0).build().start();
    private OrderRepository orders;

    @BeforeEach
    void setup() throws Exception {
        // Configure RestAssured to run tests against our application
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = SERVER.port();

        orders = SERVER.cdiContainer().select(OrderRepository.class).get();

        // oh, boy... not pretty, but probably the best we can do
        // without adding clear() to public interface
        WeldClientProxy proxy = (WeldClientProxy) orders;
        Object o = proxy.getMetadata().getContextualInstance();
        o.getClass().getMethod("clear").invoke(o);
    }

    @Test
    void testGetMissingOrder() {
        when().
                get("/orders/{orderId}", "XYZ").
        then().
                statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void testGetOrder() {
        Order order = order("homer", 1);
        orders.saveOrder(order);
        Order saved = get("/orders/{orderId}", order.getOrderId()).as(Order.class, ObjectMapperType.JSONB);

        assertThat(saved, is(order));
    }

    @Test
    void testFindOrdersByCustomerId() {
        orders.saveOrder(order("homer", 1));
        orders.saveOrder(order("homer", 2));
        orders.saveOrder(order("marge", 5));

        given().
                queryParam("custId", "homer").
        when().
                get("/orders/search/customerId").
        then().
                statusCode(OK.getStatusCode()).
                body("_embedded.customerOrders.total", containsInAnyOrder(1f, 5f));

        given().
                queryParam("custId", "marge").
        when().
                get("/orders/search/customerId").
        then().
                statusCode(OK.getStatusCode()).
                body("_embedded.customerOrders.total", containsInAnyOrder(55f));

        given().
                queryParam("custId", "bart").
        when().
                get("/orders/search/customerId").

        then().
                statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void testCreateOrder() {
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
                body("total", is(18.99f),
                     "payment.authorised", is(true),
                     "shipment.carrier", is("UPS"),
                     "shipment.deliveryDate", is(LocalDate.now().plusDays(2).toString())
                );
    }

    @Test
    void testInvalidOrder() {
        NewOrderRequest req = NewOrderRequest.builder().build();

        given().
                body(req).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
        when().
                post("/orders").
        then().
                statusCode(NOT_ACCEPTABLE.getStatusCode())
                .body("message", is("Invalid order request. Order requires customer, address, card and items."));
    }

    @Test
    void testPaymentFailure() {
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
                statusCode(NOT_ACCEPTABLE.getStatusCode())
                .body("message", is("Unable to parse authorization packet"));
    }

    @Test
    void testPaymentDeclined() {
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
                statusCode(NOT_ACCEPTABLE.getStatusCode())
                .body("message", is("Minors need parent approval"));
    }
}
