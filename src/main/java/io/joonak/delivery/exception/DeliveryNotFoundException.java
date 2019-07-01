package io.joonak.delivery.exception;

import lombok.Getter;

@Getter
public class DeliveryNotFoundException extends RuntimeException {
    private Long id;
    public DeliveryNotFoundException(Long id) {
        super("Can not found delivery for id: " + id);
        this.id = id;
    }
}
