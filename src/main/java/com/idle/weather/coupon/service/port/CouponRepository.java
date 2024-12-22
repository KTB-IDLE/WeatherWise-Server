package com.idle.weather.coupon.service.port;

import com.idle.weather.coupon.repository.CouponEntity;

import java.util.List;

public interface CouponRepository {

    CouponEntity findById(Long id);
    List<CouponEntity> findAll();

    CouponEntity findByIdForLock(Long couponId);
    void save(CouponEntity coupon);
}
