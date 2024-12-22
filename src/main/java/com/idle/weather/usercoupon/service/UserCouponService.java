package com.idle.weather.usercoupon.service;

import com.idle.weather.coupon.repository.CouponEntity;
import com.idle.weather.coupon.service.port.CouponRepository;
import com.idle.weather.usercoupon.api.response.UserCouponResponseDto;
import com.idle.weather.usercoupon.repository.UserCouponEntity;
import com.idle.weather.usercoupon.service.port.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.idle.weather.usercoupon.api.response.UserCouponResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public List<SingleUserCoupon> getUserCoupons(Long userId) {
        // UserCouponEntity 리스트 조회
        List<UserCouponEntity> userCoupons = userCouponRepository.findByUserId(userId);

        return userCoupons.stream()
                .map(userCoupon -> {
                    CouponEntity coupon = couponRepository.findById(userCoupon.getCouponId());
                    return SingleUserCoupon.of(userCoupon, coupon);
                })
                .collect(Collectors.toList());
    }
}
