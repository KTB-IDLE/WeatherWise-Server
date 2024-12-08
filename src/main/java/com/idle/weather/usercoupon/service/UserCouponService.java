package com.idle.weather.usercoupon.service;

import com.idle.weather.coupon.service.port.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final CouponRepository userCouponRepository;
}
