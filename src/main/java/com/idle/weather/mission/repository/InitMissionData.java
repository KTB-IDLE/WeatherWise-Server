package com.idle.weather.mission.repository;

import com.idle.weather.coupon.repository.CouponEntity;
import com.idle.weather.coupon.repository.DiscountType;
import com.idle.weather.coupon.service.port.CouponRepository;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.mission.service.port.MissionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitMissionData {
    private final InitMissionDataService initService;
    private final MissionRepository missionRepository;
    private final CouponRepository couponRepository;
    @PostConstruct
    public void init() {
        // DB 가 비어있을때만 초기화
        if (missionRepository.findAll().size() == 0) {
            initService.hotMissionInit();
            initService.coldMissionInit();
            initService.rainMissionInit();
            initService.couponInit();
        }

        if (couponRepository.findAll().size() == 0) {
            initService.couponInit();
        }
    }


    @Service
    @RequiredArgsConstructor
    @Transactional
    static class InitMissionDataService {
        private final MissionRepository missionRepository;
        private final CouponRepository couponRepository;

        public void hotMissionInit() {
            MissionEntity hot1 = MissionEntity.builder()
                    .missionType(MissionType.HOT)
                    .point(20)
                    .name("날씨가 더우니 양산을 챙기세요!")
                    .question("Is there a parasol in this image?")
                    .build();
            missionRepository.save(hot1.toDomain());

            MissionEntity hot2 = MissionEntity.builder()
                    .missionType(MissionType.HOT)
                    .point(40)
                    .name("날씨가 더우니 물을 챙기세요!")
                    .question("Is there a water bottle in this image")
                    .build();
            missionRepository.save(hot2.toDomain());

            MissionEntity hot3 = MissionEntity.builder()
                    .missionType(MissionType.HOT)
                    .point(30)
                    .name("날씨가 더우니 손풍기를 챙기세요!")
                    .question("Is there a hand fan in this image?")
                    .build();
            missionRepository.save(hot3.toDomain());
        }
        public void coldMissionInit() {
            MissionEntity cold1 = MissionEntity.builder()
                    .missionType(MissionType.COLD)
                    .point(70)
                    .name("날씨가 추우니 목도리를 챙기세요!")
                    .question("Is there a scarf in this image?")
                    .build();
            missionRepository.save(cold1.toDomain());

            MissionEntity cold2 = MissionEntity.builder()
                    .missionType(MissionType.COLD)
                    .point(90)
                    .name("날씨가 추우니 핫팩을 챙기세요!")
                    .question("Is there a hot pack in this image?")
                    .build();
            missionRepository.save(cold2.toDomain());

            MissionEntity cold3 = MissionEntity.builder()
                    .missionType(MissionType.COLD)
                    .point(10)
                    .name("날씨가 추우니 장갑을 챙기세요!")
                    .question("Is there a pair of gloves in this image?")
                    .build();
            missionRepository.save(cold3.toDomain());
        }

        public void rainMissionInit() {
            MissionEntity rain1 = MissionEntity.builder()
                    .missionType(MissionType.RAIN)
                    .point(50)
                    .name("비가 오고 있으니 우산을 챙기세요!")
                    .question("Is there an umbrella in this image?")
                    .build();
            missionRepository.save(rain1.toDomain());

            MissionEntity rain2 = MissionEntity.builder()
                    .missionType(MissionType.RAIN)
                    .point(60)
                    .name("비가 오고 있으니 우비를 챙기세요!")
                    .question("Is there a raincoat in this image?")
                    .build();
            missionRepository.save(rain2.toDomain());

            MissionEntity rain3 = MissionEntity.builder()
                    .missionType(MissionType.RAIN)
                    .point(10)
                    .name("비가 오고 있으니 장화를 챙기세요!")
                    .question("Is there a pair of rain boots in this image?")
                    .build();
            missionRepository.save(rain3.toDomain());

            MissionEntity sunny1 = MissionEntity.builder()
                    .missionType(MissionType.SUNNY)
                    .point(30)
                    .name("날씨가 좋으니 하늘사진 촬영해서 업로드해주세요!")
                    .question("Does this image show a photograph of the sky taken in good weather?")
                    .build();
            missionRepository.save(sunny1.toDomain());

            MissionEntity sunny2 = MissionEntity.builder()
                    .missionType(MissionType.SUNNY)
                    .point(30)
                    .name("날씨가 좋으니 자전거를 타보세요!")
                    .question("Does this image show someone riding a bicycle?")
                    .build();
            missionRepository.save(sunny2.toDomain());
        }

        public void couponInit() {
            CouponEntity coupon = CouponEntity.builder()
                    .name("에버랜드 자유이용권")
                    .discountType(DiscountType.PERCENTAGE)
                    .discountValue(50)
                    .expiresAt(LocalDateTime.of(2022,12,30,23,59))
                    .condition("당일 미션 한 개 이상 인증")
                    .quantity(1000)
                    .build();

            couponRepository.save(coupon);
        }
    }
}
