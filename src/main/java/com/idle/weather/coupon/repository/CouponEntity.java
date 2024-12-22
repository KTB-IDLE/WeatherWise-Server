package com.idle.weather.coupon.repository;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class CouponEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "coupon_issuedAt")
    private LocalDateTime issuedAt;

    @Column(name = "coupon_expiresAt")
    private LocalDateTime expiresAt;

    @Enumerated(STRING)
    private DiscountType discountType;
    private int discountValue;

    @Column(name = "coupon_name")
    private String name;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "coupon_condition")
    private String condition;

    public void issue() {
        if (this.quantity <= 0) {
            throw new BaseException(ErrorCode.COUPON_QUANTITY_EXCEEDED);
        }
        this.quantity--;
    }
}
