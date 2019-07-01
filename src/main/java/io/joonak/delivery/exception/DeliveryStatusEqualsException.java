package io.joonak.delivery.exception;

import io.joonak.delivery.domain.DeliveryStatus;
import lombok.Getter;

@Getter
public class DeliveryStatusEqualsException extends RuntimeException {
    private DeliveryStatus status;
    public DeliveryStatusEqualsException(DeliveryStatus status) {
        super("Can not change to same status. status: " + status);
        this.status = status;
    }
}
