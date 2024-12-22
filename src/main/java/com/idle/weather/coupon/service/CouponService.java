package com.idle.weather.coupon.service;

import com.idle.weather.coupon.api.response.CouponResponseDto;
import com.idle.weather.coupon.repository.CouponEntity;
import com.idle.weather.coupon.service.port.CouponRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.service.port.UserRepository;
import com.idle.weather.usercoupon.service.UserCouponService;
import com.idle.weather.usercoupon.service.port.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.idle.weather.coupon.api.response.CouponResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final MissionHistoryRepository missionHistoryRepository;

    @Transactional
    public void receiveCoupon(Long userId, Long couponId) {
        CouponEntity coupon = couponRepository.findByIdForLock(couponId);

        coupon.issue();

        // 2. 이미 발급 받은 쿠폰인지 확인
        boolean hasAlreadyCoupon = userCouponRepository.hasCoupon(userId, couponId);

        if (hasAlreadyCoupon) {
            throw new BaseException(ErrorCode.ALREADY_ISSUED_COUPON);

        }

        // 3. 당일 미션 수행했는지 확인
        boolean hasUserCompletedAnyMission = missionHistoryRepository.hasUserCompletedAnyMission(userId, LocalDateTime.now());
        if (!hasUserCompletedAnyMission) {
            throw new BaseException(ErrorCode.NOT_COMPLETED_ANY_MISSION);
        }

        userCouponRepository.issuedCoupon(userId,couponId);
    }

    public List<SingleCoupon> findAll() {
        List<CouponEntity> result = couponRepository.findAll();
        return result.stream().map(SingleCoupon::from).collect(Collectors.toList());
    }
}
