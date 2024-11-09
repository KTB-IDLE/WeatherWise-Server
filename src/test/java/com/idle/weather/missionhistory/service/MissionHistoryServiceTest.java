package com.idle.weather.missionhistory.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.idle.weather.board.domain.Board;
import com.idle.weather.location.domain.Location;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.missionhistory.service.port.AIServerClient;
import com.idle.weather.mock.*;
import com.idle.weather.user.api.request.UserRequestDto;
import com.idle.weather.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;
import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MissionHistoryServiceTest {

    private MissionHistoryServiceImpl missionHistoryService;
    private FakeUserRepository fakeUserRepository = new FakeUserRepository();
    private FakeMissionRepository fakeMissionRepository = new FakeMissionRepository();
    @Mock
    private AmazonS3Client amazonS3Client;
    @Mock
    private AIServerClient aiServerClient;
    private FakeMissionHistoryRepository fakeMissionHistoryRepository;
    private MockMultipartFile imageFile;
    private URL mockUrl;
    private ObjectMetadata mockObjectMetadata;
    private PutObjectResult mockResult;

    @BeforeEach
    void init() throws IOException {
        // Mock 이미지 파일 생성
        imageFile = new MockMultipartFile(
                "imageFile",                // 필드 이름
                "test-image.jpg",            // 원래 파일 이름
                "image/jpeg",                // MIME 타입
                // 실제 파일 경로로 지정하기
                new FileInputStream("resources/test-image.png")
        );
        // Mock 설정
        mockUrl = new URL("https://bucket-name.s3.amazonaws.com/file-key");;
        mockObjectMetadata = new ObjectMetadata();
        mockObjectMetadata.setContentType(imageFile.getContentType());
        mockObjectMetadata.setContentLength(imageFile.getSize());
        mockResult = new PutObjectResult();
        fakeMissionHistoryRepository = new FakeMissionHistoryRepository();
        FakeLevelRepository fakeLevelRepository = new FakeLevelRepository();
        FakeMissionRepository fakeMissionRepository = new FakeMissionRepository();

        this.missionHistoryService = MissionHistoryServiceImpl.builder()
                .missionHistoryRepository(fakeMissionHistoryRepository)
                .aiServerClient(aiServerClient)
                .userRepository(fakeUserRepository)
                .amazonS3Client(amazonS3Client)
                .levelRepository(fakeLevelRepository)
                .s3Bucket("test-bucket")
                .s3DomainName("test-domain-name")
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

    @AfterEach
    void clear() {
        fakeUserRepository.clear();
    }


    @Test
    public void  image_를_첨부시켜_미션_인증을_받을_수_있고_인증_성공시_경험치를_얻는다() throws Exception
    {
        //given
        // mockito 사용 법 amazonS3Client 의 어떤 함수가 호출됐을 때 어떤 값을 Return 할지 정할 수 있다.
        // when(amazonS3Client.doesObjectExist("bucket-name", "object-key")).thenReturn(true);

        // amazonS3 에 관련된 코드는 모두 Mocking
        // Mock 설정에서 anyString()으로 유연하게 설정
        when(amazonS3Client.putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenReturn(mockResult);
        // getUrl()의 인자도 anyString()을 사용하여 일치시키기
        when(amazonS3Client.getUrl(anyString(), anyString())).thenReturn(mockUrl);

        User user = fakeUserRepository.findById(1L);
        int beforePoint = user.getPoint();
        MissionHistory missionHistory = fakeMissionHistoryRepository.findById(1L);

        // AI 서버 전송 로직은 무조건 True 가 반환되도록 한다.
        // Mockito 의 when 은 정확히 동일한 인스턴스로 호출될 때만 동작하기 때문에 any() 를 사용해서 해결
        // when(mockAiserverClient.missionAuthentication(missionAuth)).thenReturn(true);
        when(aiServerClient.missionAuthentication(any(MissionAuth.class))).thenReturn(true);

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
        when(amazonS3Client.putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenReturn(mockResult);
        when(amazonS3Client.getUrl(anyString(), anyString())).thenReturn(mockUrl);

        User user = fakeUserRepository.findById(1L);
        MissionHistory missionHistory = fakeMissionHistoryRepository.findById(1L);
        when(aiServerClient.missionAuthentication(any(MissionAuth.class))).thenReturn(false);

        //when
        MissionAuthenticate missionAuthenticate =
                missionHistoryService.authMission(missionHistory.getId(), imageFile, user.getId());

        //then
        assertThat(missionAuthenticate.isAuthenticated()).isFalse();
        assertThat(missionHistory.getStoreFileName()).isNull();
    }
    //then

    @Test
    public void 유저가_성공한_미션들을_확인_할_수_있다() throws Exception
    {
        //given
        User user = fakeUserRepository.findById(1L);
        // 반드시 성공한다고 가정
        when(aiServerClient.missionAuthentication(any(MissionAuth.class))).thenReturn(true);
        when(amazonS3Client.putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .thenReturn(mockResult);
        when(amazonS3Client.getUrl(anyString(), anyString())).thenReturn(mockUrl);

        Mission mission1 = fakeMissionRepository.findById(1L);
        MissionHistory missionHistory1 = fakeMissionHistoryRepository.save(MissionHistory.of(user, mission1, MissionTime.MORNING));

        Mission mission2 = fakeMissionRepository.findById(2L);
        MissionHistory missionHistory2 = fakeMissionHistoryRepository.save(MissionHistory.of(user, mission2, MissionTime.AFTERNOON));

        Mission mission3 = fakeMissionRepository.findById(3L);
        MissionHistory missionHistory3 = fakeMissionHistoryRepository.save(MissionHistory.of(user, mission3, MissionTime.EVENING));

        // 인증 로직
        missionHistoryService.authMission(missionHistory1.getId(),imageFile,user.getId());
        missionHistoryService.authMission(missionHistory2.getId(),imageFile,user.getId());
        missionHistoryService.authMission(missionHistory3.getId(),imageFile,user.getId());

        //when

        SuccessMissionHistories successMissions = missionHistoryService.getSuccessMissions(user.getId());

        //then
        assertThat(successMissions.getMissionList().size()).isEqualTo(3);
        assertThat(successMissions.getMissionList().get(0).isCompleted()).isTrue();
    }
}