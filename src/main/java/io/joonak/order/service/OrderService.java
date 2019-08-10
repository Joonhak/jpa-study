package io.joonak.order.service;

import io.joonak.coupon.domain.Coupon;
import io.joonak.coupon.service.CouponService;
import io.joonak.order.domain.Order;
import io.joonak.order.exception.OrderNotFoundException;
import io.joonak.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponService couponService;

    public Order order() {
        final Order order = Order.builder().price(10_000D).build();
        Coupon coupon = couponService.findById(1L);
        order.applyCoupon(coupon);
        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

}
