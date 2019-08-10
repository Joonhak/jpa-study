package io.joonak.coupon.exception;

import lombok.Getter;

@Getter
public class CouponNotFoundException extends RuntimeException {
    private Long id;
    public CouponNotFoundException(Long id) {
        this.id = id;
    }
}
