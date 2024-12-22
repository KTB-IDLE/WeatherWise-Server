package com.idle.weather.coupon.repository;

import com.idle.weather.coupon.service.port.CouponRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponRepository;

    @Override
    public CouponEntity findById(Long id) {
        return couponRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COUPON));
    }

    @Override
    public List<CouponEntity> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public CouponEntity findByIdForLock(Long couponId) {
        return couponRepository.findByIdWithPessimisticLock(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_COUPON));
    }
}
