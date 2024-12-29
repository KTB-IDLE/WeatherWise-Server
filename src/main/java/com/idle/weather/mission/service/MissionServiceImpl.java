package com.idle.weather.mission.service;

import com.idle.weather.mission.api.port.MissionService;
import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.api.response.MissionResponseDto;
import com.idle.weather.mission.domain.CurrentWeatherResponse;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.mission.repository.MissionEntity;
import com.idle.weather.mission.service.port.MissionRepository;
import com.idle.weather.missionhistory.api.port.MissionHistoryService;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import com.idle.weather.mock.MockFastApiService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;
import static com.idle.weather.mission.api.response.MissionResponseDto.*;

@Service @Builder
@RequiredArgsConstructor
@Transactional @Slf4j
public class MissionServiceImpl implements MissionService {
    private final MissionRepository missionRepository;
    private final MissionHistoryRepository missionHistoryRepository;
    private final UserRepository userRepository;
    private final MockFastApiService fastApiService;
    private final Random random = new Random();
    @Override
    public SingleMission createMission(Long userId, CreateMission createMission) {
        User user = userRepository.findById(userId);

        // 오늘의 미션 중 중복된 미션 확인
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        List<Long> existingMissionIds = missionHistoryRepository.findMissionIdsByUserAndDateRange(
                user.getId(), startOfDay, endOfDay
        );

        // 중복되지 않은 미션 중 하나를 무작위로 선택
        List<MissionType> types = Arrays.asList(MissionType.SUNNY, MissionType.COLD);
        List<MissionEntity> availableMissions = missionRepository.findRandomMissionExcluding(existingMissionIds, types);
        MissionEntity missionEntity = availableMissions.get(new Random().nextInt(availableMissions.size()));
        Mission mission = missionEntity.toDomain();

        // MissionHistory 저장
        MissionHistory missionHistory = missionHistoryRepository.save(MissionHistory.of(user, mission, MissionTime.fromValue(createMission.missionTime())));

        return SingleMission.of(mission , missionHistory.getId());
    }
}
