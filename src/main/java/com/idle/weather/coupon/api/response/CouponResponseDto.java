package com.idle.weather.coupon.api.response;

import com.idle.weather.coupon.repository.CouponEntity;

public record CouponResponseDto(
        Long id,
        String name,
        String condition,
        int quantity
) {
    public static CouponResponseDto from(CouponEntity coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getName(),
                coupon.getCondition(),
                coupon.getQuantity()
        );
    }
}