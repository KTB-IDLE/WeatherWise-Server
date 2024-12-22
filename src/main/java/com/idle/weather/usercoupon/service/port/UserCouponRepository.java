package com.idle.weather.usercoupon.service.port;


import com.idle.weather.usercoupon.repository.UserCouponEntity;

import java.util.List;

public interface UserCouponRepository {
    boolean hasCoupon(Long userId, Long couponId);
    void issuedCoupon(Long userId , Long conpouId);
    List<UserCouponEntity> findByUserId(Long userId);
}
