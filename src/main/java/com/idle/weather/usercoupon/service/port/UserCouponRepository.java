package com.idle.weather.usercoupon.service.port;


public interface UserCouponRepository {
    boolean hasCoupon(Long userId, Long couponId);
    void issuedCoupon(Long userId , Long conpouId);
}
