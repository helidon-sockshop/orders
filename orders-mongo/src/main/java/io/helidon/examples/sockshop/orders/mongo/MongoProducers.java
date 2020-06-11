/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package io.helidon.examples.sockshop.orders.mongo;

import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.helidon.config.Config;
import io.helidon.examples.sockshop.orders.Order;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.java.Log;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import static java.lang.String.format;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * CDI support for MongoDB.
 */
@ApplicationScoped
@Log
public class MongoProducers {

    /**
     * Default MongoDB host to connect to.
     */
    public static final String DEFAULT_HOST = "orders-db";

    /**
     * Default MongoDB port to connect to.
     */
    public static final int DEFAULT_PORT = 27017;

    /**
     * CDI Producer for {@code MongoClient}.
     *
     * @param config application configuration, which will be used to read
     *               the values of a {@code db.host} and {@code db.port}
     *               configuration properties, if present. If either is not
     *               present, the defaults defined by the {@link #DEFAULT_HOST}
     *               and {@link #DEFAULT_PORT} constants will be used.
     *
     * @return a {@code MongoClient} instance
     */
    @Produces
    @ApplicationScoped
    public static MongoClient client(Config config) {
        Config orders = config.get("db");
        return client(orders.get("host").asString().orElse(DEFAULT_HOST),
                      orders.get("port").asInt().orElse(DEFAULT_PORT));
    }

    /**
     * CDI Producer for {@code MongoDatabase}.
     *
     * @param client the MongoDB client to use
     *
     * @return a {@code MongoDatabase} instance
     */
    @Produces
    @ApplicationScoped
    public static MongoDatabase db(MongoClient client) {
        return client.getDatabase("orders");
    }

    /**
     * CDI Producer for the {@code MongoCollection} that contains orders.
     *
     * @param db the MongoDB database to use
     *
     * @return a {@code MongoCollection} containing the orders
     */
    @Produces
    @ApplicationScoped
    public static MongoCollection<Order> orders(MongoDatabase db) {
        return db.getCollection("orders", Order.class);
    }

    // ---- helpers ---------------------------------------------------------

    /**
     * Create {@code MongoClient} for the specified host and port.
     *
     * @param host the MongoDB host to connect to
     * @param port the MongoDB port to connect to
     *
     * @return a {@code MongoClient} instance
     */
    static MongoClient client(String host, int port) {
        log.info(format("Connecting to MongoDB on host %s:%d", host, port));
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                                                         fromProviders(PojoCodecProvider.builder()
                                                                               .automatic(true)
                                                                               .conventions(Conventions.DEFAULT_CONVENTIONS)
                                                                               .build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applicationName("orders")
                .applyToClusterSettings(
                        builder -> builder.hosts(Collections.singletonList(new ServerAddress(host, port))))
                .codecRegistry(pojoCodecRegistry)
                .build();
        return MongoClients.create(settings);
    }

}
