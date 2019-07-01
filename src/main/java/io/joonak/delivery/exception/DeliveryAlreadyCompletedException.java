package io.joonak.delivery.exception;

import lombok.Getter;

@Getter
public class DeliveryAlreadyCompletedException extends RuntimeException {
    private Long id;
    public DeliveryAlreadyCompletedException(Long id) {
        super("Delivery has already been completed. delivery id: " + id);
        this.id = id;
    }
}
