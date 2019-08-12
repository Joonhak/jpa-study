package io.joonak.coupon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.joonak.order.domain.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "is_used", columnDefinition = "tinyint(1)")
    private boolean use;

    @JsonIgnore
    @OneToOne(mappedBy = "coupon")
    private Order order;

    @Builder
    public Coupon(Double discountAmount) {
        this.discountAmount = discountAmount;
        this.use = false;
    }

    public void use(Order order) {
        this.order = order;
        this.use = true;
    }

}
