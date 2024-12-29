package com.idle.weather.usercoupon.api.response;

import com.idle.weather.coupon.repository.CouponEntity;
import com.idle.weather.usercoupon.repository.UserCouponEntity;

import java.time.LocalDateTime;

public record UserCouponResponseDto() {

    public record SingleUserCoupon(
            Long userCouponId,
            String name,
            int disCountValue,
            LocalDateTime expirationDate,
            boolean isUsed
    ) {
        public static SingleUserCoupon of(UserCouponEntity userCoupon, CouponEntity coupon) {
            return new SingleUserCoupon(
                    userCoupon.getId(),
                    coupon.getName(),
                    coupon.getDiscountValue(),
                    coupon.getExpiresAt(),
                    userCoupon.isUsed()
            );
        }
    }
}