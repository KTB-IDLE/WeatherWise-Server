package com.idle.weather.coupon.api.response;

import com.idle.weather.coupon.repository.CouponEntity;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

public class CouponResponseDto {

    @Builder
    @Getter
    public static class SingleCoupon {
        private Long id;
        private String name;
        private String condition;
        private int quantity;

        public static SingleCoupon from(CouponEntity coupon) {
            return SingleCoupon.builder()
                    .id(coupon.getId())
                    .name(coupon.getName())
                    .condition(coupon.getCondition())
                    .quantity(coupon.getQuantity())
                    .build();
        }
    }
}
