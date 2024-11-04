package com.idle.weather.mission.service;

import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.api.response.MissionResponseDto;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.mock.FakeMissionHistoryRepository;
import com.idle.weather.mock.FakeMissionRepository;
import com.idle.weather.mock.FakeUserRepository;
import com.idle.weather.mock.MockFastApiService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;
import static com.idle.weather.mission.api.response.MissionResponseDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MissionServiceTest {

    private MissionServiceImpl missionService;
    @BeforeEach
    void init() {
        FakeMissionRepository fakeMissionRepository = new FakeMissionRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.missionService = MissionServiceImpl.builder()
                .fastApiService(new MockFastApiService())
                .missionRepository(fakeMissionRepository)
                .missionHistoryRepository(new FakeMissionHistoryRepository())
                .userRepository(fakeUserRepository)
                .build();
        User user = User.builder()
                .id(1L)
                .missionHistories(new ArrayList<>())
                .nickname("CIAN")
                .password("!234")
                .isCompletedSurvey(true)
                .easilyCold(true)
                .easilyHot(false)
                .easilySweat(true)
                .level(1)
                .point(0)
                .build();
        fakeUserRepository.save(user);

        Mission hotMission = Mission.builder()
                .id(1L)
                .missionType(MissionType.HOT)
                .point(50)
                .name("HOT 테스트")
                .description("HOT 테스트")
                .build();
        fakeMissionRepository.save(hotMission);

        Mission coldMission = Mission.builder()
                .id(2L)
                .missionType(MissionType.COLD)
                .point(50)
                .name("COLD 테스트")
                .description("COLD 테스트")
                .build();
        fakeMissionRepository.save(coldMission);

        Mission rainMission = Mission.builder()
                .id(3L)
                .missionType(MissionType.RAIN)
                .point(50)
                .name("RAIN 테스트")
                .description("RAIN 테스트")
                .build();
        fakeMissionRepository.save(rainMission);

        Mission snowMission = Mission.builder()
                .id(4L)
                .missionType(MissionType.SNOW)
                .point(50)
                .name("SNOW 테스트")
                .description("SNOW 테스트")
                .build();
        fakeMissionRepository.save(snowMission);
    }

    @Test
    public void CreateMissionDto_을_통해_미션을_생성_할_수_있다() throws Exception
    {
        //given
        // 아침 , 위도 및 경도가 50,50 인 경우
        MissionTime time = MissionTime.MORNING;
        int nx = 50;
        int ny = 50;
        CreateMission createMissionDto = CreateMission.builder()
                .missionTime(time.toString())
                .nx(nx)
                .ny(ny)
                .build();

        //when
        SingleMission mission = missionService.createMission(1L, createMissionDto);

        //then

        // 바로 생성하면 MissionHistory 의 상태는 완료 X
        assertThat(mission.isCompleted()).isFalse();
        assertThat(mission.getId()).isNotNull();
    }

}