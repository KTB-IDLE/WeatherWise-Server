package com.idle.weather.mission.repository;

import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.mission.service.port.MissionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitMissionData {
    private final InitMissionDataService initService;
    private final MissionRepository missionRepository;
    @PostConstruct
    public void init() {
        // DB 가 비어있을때만 초기화
        if (missionRepository.findAll().size() == 0) {
            initService.hotMissionInit();
            initService.coldMissionInit();
            initService.rainMissionInit();
        }
    }


    @Service
    @RequiredArgsConstructor
    @Transactional
    static class InitMissionDataService {
        private final MissionRepository missionRepository;

        public void hotMissionInit() {
            MissionEntity hot1 = MissionEntity.builder()
                    .missionType(MissionType.HOT)
                    .point(20)
                    .name("날씨가 더우니 양산을 챙기세요!")
                    .build();
            missionRepository.save(hot1);

            MissionEntity hot2 = MissionEntity.builder()
                    .missionType(MissionType.HOT)
                    .point(40)
                    .name("날씨가 더우니 물을 챙기세요!")
                    .build();
            missionRepository.save(hot2);

            MissionEntity hot3 = MissionEntity.builder()
                    .missionType(MissionType.HOT)
                    .point(30)
                    .name("날씨가 더우니 손풍기를 챙기세요!")
                    .build();
            missionRepository.save(hot3);
        }
        public void coldMissionInit() {
            MissionEntity cold1 = MissionEntity.builder()
                    .missionType(MissionType.COLD)
                    .point(70)
                    .name("날씨가 추우니 목도리를 챙기세요!")
                    .build();
            missionRepository.save(cold1);

            MissionEntity cold2 = MissionEntity.builder()
                    .missionType(MissionType.COLD)
                    .point(90)
                    .name("날씨가 추우니 핫팩을 챙기세요!")
                    .build();
            missionRepository.save(cold2);

            MissionEntity cold3 = MissionEntity.builder()
                    .missionType(MissionType.COLD)
                    .point(10)
                    .name("날씨가 추우니 장갑을 챙기세요!")
                    .build();
            missionRepository.save(cold3);
        }

        public void rainMissionInit() {
            MissionEntity rain1 = MissionEntity.builder()
                    .missionType(MissionType.RAIN)
                    .point(50)
                    .name("비가 오고 있으니 우산을 챙기세요!")
                    .build();
            missionRepository.save(rain1);

            MissionEntity rain2 = MissionEntity.builder()
                    .missionType(MissionType.RAIN)
                    .point(60)
                    .name("비가 오고 있으니 우비를 챙기세요!")
                    .build();
            missionRepository.save(rain2);

            MissionEntity rain3 = MissionEntity.builder()
                    .missionType(MissionType.RAIN)
                    .point(10)
                    .name("비가 오고 있으니 장화를 챙기세요!")
                    .build();
            missionRepository.save(rain3);
        }
    }
}
