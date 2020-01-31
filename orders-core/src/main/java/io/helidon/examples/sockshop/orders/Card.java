package io.helidon.examples.sockshop.orders;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Credit card information.
 */
@Data
@NoArgsConstructor
@Embeddable
public class Card implements Serializable {
    /**
     * Credit card number.
     */
    private String longNum;

    /**
     * Expiration date.
     */
    private String expires;

    /**
     * CCV code.
     */
    private String ccv;

    @Builder
    Card(String longNum, String expires, String ccv) {
        this.longNum = longNum;
        this.expires = expires;
        this.ccv = ccv;
    }
}
