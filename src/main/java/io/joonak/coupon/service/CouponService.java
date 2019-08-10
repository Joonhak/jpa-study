package io.joonak.coupon.service;

import io.joonak.coupon.domain.Coupon;
import io.joonak.coupon.exception.CouponNotFoundException;
import io.joonak.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {

    private CouponRepository couponRepository;

    public Coupon findById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
    }
}
