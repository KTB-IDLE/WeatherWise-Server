package com.idle.weather.usercoupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCouponJpaRepository extends JpaRepository<UserCouponEntity, Long> {
    @Query("SELECT COUNT(u) > 0 FROM UserCouponEntity u WHERE u.userId = :userId AND u.couponId = :couponId")
    boolean existsByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    List<UserCouponEntity> findByUserId(Long userId);
}
