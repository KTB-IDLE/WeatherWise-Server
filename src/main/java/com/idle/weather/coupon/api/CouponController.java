package com.idle.weather.coupon.api;

import com.idle.weather.common.annotation.UserId;
import com.idle.weather.coupon.api.response.CouponResponseDto;
import com.idle.weather.coupon.service.CouponService;
import com.idle.weather.coupon.service.port.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.idle.weather.coupon.api.response.CouponResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/{coupon-id}")
    public void receiveCoupon(@UserId Long userId , @PathVariable("coupon-id") Long couponId) {
        couponService.receiveCoupon(userId , couponId);
    }

    @GetMapping
    public List<SingleCoupon> getCoupons() {
        return couponService.findAll();
    }

}
