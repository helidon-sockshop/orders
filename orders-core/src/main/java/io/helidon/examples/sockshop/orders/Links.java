package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import io.helidon.common.CollectionsHelper;

public class Links extends LinkedHashMap<String, Links.Link> implements Serializable {
    private static Map<String, String> ENTITY_MAP = CollectionsHelper.mapOf("order", "orders");

    private Links addLink(String entity, String id) {
        Link link = Link.to(ENTITY_MAP.get(entity), id);
        put(entity, link);
        put("self", link);
        return this;
    }

    private Links addAttrLink(String entity, String id, String attr) {
        Link link = Link.to(ENTITY_MAP.get(entity), id, attr);
        put(attr, link);
        return this;
    }

    public static Links order(String id) {
        return new Links()
            .addLink("order", id);
    }

    public static Links address(String id) {
        return new Links().addLink("address", id);
    }

    public static Links card(String id) {
        return new Links().addLink("card", id);
    }

    public static class Link implements Serializable {
        public String href;

        public Link() {
        }

        Link(String href) {
            this.href = href;
        }

        static Link to(Object... pathElements) {
            StringBuilder sb = new StringBuilder("http://orders");
            for (Object e : pathElements) {
                sb.append('/').append(e);
            }
            return new Link(sb.toString());
        }
    }
}
