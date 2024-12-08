package com.idle.weather.coupon.service.port;

import com.idle.weather.coupon.repository.CouponEntity;

public interface CouponRepository {

    CouponEntity findById(Long id);
}
