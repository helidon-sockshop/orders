package io.helidon.examples.sockshop.orders;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payment authorization received from a Payment service.
 */
@Data
@NoArgsConstructor
@Embeddable
public class Payment {
    /**
     * Flag specifying whether the payment was authorized.
     */
    private boolean authorised;

    /**
     * Approval or rejection message.
     */
    private String  message;

    @Builder
    Payment(boolean authorised, String message) {
        this.authorised = authorised;
        this.message = message;
    }
}
