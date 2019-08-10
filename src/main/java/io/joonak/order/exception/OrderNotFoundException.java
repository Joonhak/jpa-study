package io.joonak.order.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {
    private Long id;
    public OrderNotFoundException(Long id) {
        this.id = id;
    }
}
