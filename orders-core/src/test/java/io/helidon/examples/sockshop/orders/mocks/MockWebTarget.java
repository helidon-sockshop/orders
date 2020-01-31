package io.helidon.examples.sockshop.orders.mocks;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * Mock WebTarget, just passing through it.
 */
public class MockWebTarget implements WebTarget {
    private URI uri;

    MockWebTarget(URI uri) {
        this.uri = uri;
    }

    // -- the only method we care about, which gets us to the MockInvocationBuilder

    @Override
    public Invocation.Builder request(String... mediaTypes) {
        // don't care about media types, it's always JSON
        return new MockInvocationBuilder(uri);
    }

    // -- everything else can remain unimplemented

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public UriBuilder getUriBuilder() {
        return null;
    }

    @Override
    public WebTarget path(String s) {
        return null;
    }

    @Override
    public WebTarget resolveTemplate(String s, Object o) {
        return null;
    }

    @Override
    public WebTarget resolveTemplate(String s, Object o, boolean b) {
        return null;
    }

    @Override
    public WebTarget resolveTemplateFromEncoded(String s, Object o) {
        return null;
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> map) {
        return null;
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> map, boolean b) {
        return null;
    }

    @Override
    public WebTarget resolveTemplatesFromEncoded(Map<String, Object> map) {
        return null;
    }

    @Override
    public WebTarget matrixParam(String s, Object... objects) {
        return null;
    }

    @Override
    public WebTarget queryParam(String s, Object... objects) {
        return null;
    }

    @Override
    public Invocation.Builder request() {
        return null;
    }

    @Override
    public Invocation.Builder request(MediaType... mediaTypes) {
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public WebTarget property(String s, Object o) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass, int i) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass, Class<?>... classes) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass, Map<Class<?>, Integer> map) {
        return null;
    }

    @Override
    public WebTarget register(Object o) {
        return null;
    }

    @Override
    public WebTarget register(Object o, int i) {
        return null;
    }

    @Override
    public WebTarget register(Object o, Class<?>... classes) {
        return null;
    }

    @Override
    public WebTarget register(Object o, Map<Class<?>, Integer> map) {
        return null;
    }
}
