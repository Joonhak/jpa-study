package io.joonak.order;

import io.joonak.coupon.domain.Coupon;
import io.joonak.coupon.service.CouponService;
import io.joonak.order.domain.Order;
import io.joonak.order.exception.OrderNotFoundException;
import io.joonak.order.repository.OrderRepository;
import io.joonak.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CouponService couponService;
    @Mock
    private OrderRepository orderRepository;

    private Coupon coupon = Coupon.builder().discountAmount(1_000D).build();
    private Order order = Order.builder().price(10_000D).build();

    @Test
    public void 존재하는_주문() {
        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when
        var result = orderService.findById(1L);

        // then
        assertThat(result.getPrice(), is(10_000D));
    }

    @Test
    public void 존재하지_않는_주문() {
        // given
        given(orderRepository.findById(any(Long.class)))
                .willThrow(OrderNotFoundException.class);

        // when
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(any(Long.class)));

        // then
        // throw exception
    }

    @Test
    public void 주문에_쿠폰_적용() {
        // given
        order.applyCoupon(coupon);
        given(couponService.findById(anyLong()))
                .willReturn(coupon);
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);

        // when
        var result = orderService.order();

        // then
        assertThat(result.getPrice(), is(9_000D));
        assertThat(coupon.isUse(), is(true));
    }

}
