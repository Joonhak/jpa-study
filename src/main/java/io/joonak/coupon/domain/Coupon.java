package io.joonak.coupon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.joonak.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "use")
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
