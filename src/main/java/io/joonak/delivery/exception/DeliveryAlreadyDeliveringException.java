package io.joonak.delivery.exception;

import lombok.Getter;

@Getter
public class DeliveryAlreadyDeliveringException extends RuntimeException {
    private Long id;
    public DeliveryAlreadyDeliveringException(Long id) {
        super("Delivery already started. delivery id: " + id);
        this.id = id;
    }
}
