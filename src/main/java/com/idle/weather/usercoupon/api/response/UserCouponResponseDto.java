package com.idle.weather.usercoupon.api.response;

import com.idle.weather.coupon.repository.CouponEntity;
import com.idle.weather.usercoupon.repository.UserCouponEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserCouponResponseDto {

    @Getter @Builder
    public static class SingleUserCoupon {
        private Long userCouponId;
        private String name;
        private int disCountValue;
        private LocalDateTime expirationDate;
        private boolean isUsed;

        public static SingleUserCoupon of(UserCouponEntity userCoupon , CouponEntity coupon) {
            return SingleUserCoupon.builder()
                    .name(coupon.getName())
                    .disCountValue(coupon.getDiscountValue())
                    .expirationDate(coupon.getExpiresAt())
                    .isUsed(userCoupon.isUsed())
                    .userCouponId(userCoupon.getId())
                    .build();
        }


    }
}
