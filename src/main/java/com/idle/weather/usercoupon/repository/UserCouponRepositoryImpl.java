package com.idle.weather.usercoupon.repository;

import com.idle.weather.coupon.repository.CouponJpaRepository;
import com.idle.weather.coupon.service.port.CouponRepository;
import com.idle.weather.usercoupon.service.port.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponJpaRepository userCouponRepository;

    @Override
    public boolean hasCoupon(Long userId, Long couponId) {
        return userCouponRepository.existsByUserIdAndCouponId(userId , couponId);
    }

    @Override
    public void issuedCoupon(Long userId, Long couponId) {
        UserCouponEntity userCoupon = UserCouponEntity.issuedCoupon(userId, couponId);
        userCouponRepository.save(userCoupon);
    }

    @Override
    public List<UserCouponEntity> findByUserId(Long userId) {
        return userCouponRepository.findByUserId(userId);
    }
}
