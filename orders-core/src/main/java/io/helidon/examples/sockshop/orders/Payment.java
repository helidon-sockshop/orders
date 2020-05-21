package io.helidon.examples.sockshop.orders;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Payment authorization received from a Payment service.
 */
@Data
@NoArgsConstructor
@Embeddable
public class Payment implements Serializable {
    /**
     * Flag specifying whether the payment was authorized.
     */
    @Schema(description = "Flag specifying whether the payment was authorized")
    private boolean authorised;

    /**
     * Approval or rejection message.
     */
    @Schema(description = "Approval or rejection message")
    private String  message;

    @Builder
    Payment(boolean authorised, String message) {
        this.authorised = authorised;
        this.message = message;
    }
}
