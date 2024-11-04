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