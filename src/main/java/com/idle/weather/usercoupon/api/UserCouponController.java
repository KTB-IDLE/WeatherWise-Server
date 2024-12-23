package com.idle.weather.usercoupon.api;

import com.idle.weather.common.annotation.UserId;
import com.idle.weather.coupon.service.CouponService;
import com.idle.weather.usercoupon.api.response.UserCouponResponseDto;
import com.idle.weather.usercoupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.idle.weather.usercoupon.api.response.UserCouponResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-coupons")
public class UserCouponController {
    private final UserCouponService userCouponService;

    @GetMapping
    public List<SingleUserCoupon> getUserCoupons(@UserId Long userId) {
        return userCouponService.getUserCoupons(userId);
    }

    @PostMapping("/{user-coupon-id}")
    public void useCoupon(@UserId Long userId , @PathVariable("user-coupon-id") Long userCouponId) {
        userCouponService.useCoupon(userId , userCouponId);
    }
}
