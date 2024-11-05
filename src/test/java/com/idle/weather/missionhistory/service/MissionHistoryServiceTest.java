package com.idle.weather.missionhistory.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.mock.*;
import com.idle.weather.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.ArrayList;

import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MissionHistoryServiceTest {

    private MissionHistoryServiceImpl missionHistoryService;
    private FakeUserRepository fakeUserRepository;
    private FakeMissionHistoryRepository fakeMissionHistoryRepository;

    @BeforeEach
    void init() {
        fakeMissionHistoryRepository = new FakeMissionHistoryRepository();
        fakeUserRepository = new FakeUserRepository();
        FakeLevelRepository fakeLevelRepository = new FakeLevelRepository();
        FakeMissionRepository fakeMissionRepository = new FakeMissionRepository();
        this.missionHistoryService = MissionHistoryServiceImpl.builder()
                .missionHistoryRepository(fakeMissionHistoryRepository)
                .mockFastApiService(new MockFastApiService())
                .amazonS3Client(new AmazonS3Client())
                .build();



        // mission 으로 부터 만들어진 MissionHistory
        Mission mission = fakeMissionRepository.findById(1L);
        MissionHistory missionHistory = MissionHistory.builder()
                .id(1L)
                .missionTime(MissionTime.MORNING)
                .mission(mission)
                .isCompleted(false)
                .build();
        fakeMissionHistoryRepository.save(missionHistory);

        // 사용자
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
                .point(10)
                .build();
        fakeUserRepository.save(user);
    }


    @Test
    public void image_를_첨부시켜_미션_인증을_받을_수_있고_인증_성공시_경험치를_얻는다() throws Exception
    {
        // TODO: 11/4/24 Mock 을 사용하는건가? DIP 를 사용해서 해결할 수 있는건가? 
        //given
        // 가짜 이미지 파일 생성
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",                // 필드 이름
                "test-image.jpg",            // 원래 파일 이름
                "image/jpeg",                // MIME 타입
                new FileInputStream("/Users/supportkim/Desktop/dev/WeatherWise-Server/src/test/resources/test-image.png") // 실제 파일 경로
        );


        User user = fakeUserRepository.findById(1L);
        int beforePoint = user.getPoint();
        MissionHistory missionHistory = fakeMissionHistoryRepository.findById(1L);


        //when
        MissionAuthenticate missionAuthenticate =
                missionHistoryService.authMission(missionHistory.getId(), imageFile, user.getId());
        //then
        // 인증 성공시에는 인증 완료
        assertThat(missionAuthenticate.isAuthenticated()).isTrue();

        // 현재 User 의 포인트 = 기존 포인트 + 미션 포인트
        assertThat(user.getPoint()).isEqualTo(beforePoint + missionAuthenticate.getMissionExp());
    }
    @Test
    public void image_를_첨부시켜_미션_인증을_받을_수_있고_실패시_다시_이미지를_첨부_할_수_있다() throws Exception
    {
        //given
        // MissionAuthenticate missionAuthenticate = missionHistoryService.authMission(1L, , 1L);

        //when

    }
    //then

    @Test
    public void 유저가_성공한_미션들을_확인_할_수_있다() throws Exception
    {
        //given

        //when

        //then
    }

    @Test
    public void 유저가_작성한_게시글들을_확인_할_수_있다() throws Exception
    {
        //given

        //when

        //then
    }


}