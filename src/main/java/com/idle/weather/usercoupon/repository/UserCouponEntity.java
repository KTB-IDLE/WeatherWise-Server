package com.idle.weather.usercoupon.repository;

import com.idle.weather.coupon.repository.DiscountType;
import com.idle.weather.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserCouponEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "user_id")
    private Long userId;

    private boolean isUsed;

    @Column(name = "userd_at")
    private LocalDateTime usedAt; // 사용 날짜

    @Column(name = "acquired_at")
    private LocalDateTime acquiredAt; // 획득 날짜

    public static UserCouponEntity issuedCoupon(Long userId , Long couponId) {
        return UserCouponEntity.builder()
                .couponId(couponId)
                .userId(userId)
                .isUsed(false)
                .build();
    }

}
