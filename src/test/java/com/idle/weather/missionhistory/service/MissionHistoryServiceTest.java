package com.idle.weather.missionhistory.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.mock.*;
import com.idle.weather.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MissionHistoryServiceTest {

    private MissionHistoryServiceImpl missionHistoryService;
    private FakeUserRepository fakeUserRepository;

    @Mock
    private AmazonS3Client amazonS3Client;
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
                new FileInputStream("/Users/supportkim/Desktop/dev/WeatherWise-Server/src/test/resources/test-image.png") // 실제 파일 경로
        );
        // Mock 설정
        mockUrl = new URL("https://bucket-name.s3.amazonaws.com/file-key");;
        mockObjectMetadata = new ObjectMetadata();
        mockObjectMetadata.setContentType(imageFile.getContentType());
        mockObjectMetadata.setContentLength(imageFile.getSize());
        mockResult = new PutObjectResult();

        fakeMissionHistoryRepository = new FakeMissionHistoryRepository();
        fakeUserRepository = new FakeUserRepository();
        FakeLevelRepository fakeLevelRepository = new FakeLevelRepository();
        FakeMissionRepository fakeMissionRepository = new FakeMissionRepository();
        this.missionHistoryService = MissionHistoryServiceImpl.builder()
                .missionHistoryRepository(fakeMissionHistoryRepository)
                .mockFastApiService(new MockFastApiService())
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