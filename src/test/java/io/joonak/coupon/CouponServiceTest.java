package io.joonak.coupon;

import io.joonak.coupon.domain.Coupon;
import io.joonak.coupon.exception.CouponNotFoundException;
import io.joonak.coupon.repository.CouponRepository;
import io.joonak.coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    private Coupon coupon = Coupon.builder().discountAmount(1_000D).build();

    @Test
    public void 쿠폰_조회() {
        // given
        given(couponRepository.findById(anyLong()))
                .willReturn(Optional.of(coupon));

        // when
        var result = couponService.findById(1L);

        // then
        assertThat(result.getDiscountAmount(), is(1_000D));
    }

    @Test
    public void 없는_쿠폰_조회() {
        // given
        given(couponRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        assertThrows(CouponNotFoundException.class, () -> couponService.findById(1L));
    }

}
