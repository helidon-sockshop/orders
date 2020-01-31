package io.helidon.examples.sockshop.orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer information.
 */
@Data
@NoArgsConstructor
@Embeddable
public class Customer implements Serializable {
    /**
     * Customer identifier.
     */
    @JsonbProperty("username")
    private String id;

    /**
     * First name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Customer's email.
     */
    private String email;

    @Builder
    Customer(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
