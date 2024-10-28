package com.idle.weather.missionhistory.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.repository.LevelEntity;
import com.idle.weather.level.repository.LevelJpaRepository;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.api.port.MissionHistoryService;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import com.idle.weather.mock.MockFastApiService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionHistoryServiceImpl implements MissionHistoryService {
    private final MissionHistoryRepository missionHistoryRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;
    private final LevelJpaRepository levelRepository;

    // Mock Server
    private final MockFastApiService mockFastApiService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String pythonServerUrl = "http://python-server-url/authenticate";

    @Override
    public MissionHistoriesInfo getMissionList(LocalDate date , Long userId) {
        User user = userRepository
                .findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        List<MissionHistory> missionHistoryByDate = missionHistoryRepository.findMissionHistoryByDate(user.getId(), date);
        // Domain -> DTO 변환
        List<SingleMissionHistory> result = missionHistoryByDate.stream().map(SingleMissionHistory::from).toList();
        return MissionHistoriesInfo.of(user.getNickname(),result);
    }

    @Override
    public MissionAuthenticationView getMission(Long missionHistoryId , Long userId) {
        User user = userRepository
                .findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();
        MissionHistory missionHistory = missionHistoryRepository.findById(missionHistoryId);
        return MissionAuthenticationView.of(user.getNickname() , missionHistory);
    }

    @Override
    @Transactional
    public MissionHistory save(Long userId , Mission mission, MissionTime missionTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        return missionHistoryRepository.save(MissionHistory.of(user, mission,missionTime));
    }

    @Override
    @Transactional
    public MissionAuthenticate authMission(Long missionHistoryId,
                                           MultipartFile imageFile,
                                           Long userId) throws IOException {
        User user = userRepository
                .findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        // 이미지 인증 결과
        boolean authenticationResult  = sendFastAPIServer(imageFile);

        // 인증 실패시
        if (!authenticationResult) {
            return MissionAuthenticate.fail();
        }

        // 인증 성공시
        MissionHistoryEntity missionHistoryEntity = missionHistoryRepository.findByIdEntity(missionHistoryId);
        MissionHistory missionHistory = missionHistoryEntity.toDomain();
        Mission mission = missionHistory.getMission();

        // User 경험치 추가

        // 경험치 추가 할 때 레벨 계산하기
        LevelEntity level = levelRepository.findById(user.getLevel())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LEVEL));
        int totalPoint = user.getPoint() + mission.getPoint();
        if (totalPoint >= level.getMaxExp()) {
            user.levelUp(totalPoint-level.getMaxExp());
        }

        user.updatedExperience(mission.getPoint());

        // MissionHistory 인증 성공으로 업데이트
        missionHistory.updateCompleted();

        // Entity 에 반영하기
        missionHistoryEntity.updateCompleted(missionHistory);

        return MissionAuthenticate.of(authenticationResult , mission.getPoint() ,
                user.getLevel() ,user.getPoint(),level.getMaxExp());
    }

    @Override
    public SuccessMissionHistories getSuccessMissions(Long userId) {

        User user = userRepository
                .findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        List<MissionHistory> userMissionHistories = user.getMissionHistories();
        List<SingleMissionHistory> userSuccessMissionHistories = userMissionHistories.stream()
                .filter(MissionHistory::isCompleted)
                .map(SingleMissionHistory::from)
                .toList();

        return SuccessMissionHistories.from(userSuccessMissionHistories);
    }

    private boolean sendFastAPIServer(MultipartFile imageFile) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Multipart 요청 바디에 파일을 포함
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("imageFile", convertMultipartFileToResource(imageFile));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 파이썬 서버로 이미지 파일 POST 요청 전송
        /*ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(
                pythonServerUrl, requestEntity, Boolean.class
        );*/
        // return responseEntity.getBody();

        /**
         * 우선 Mock 서버로 대체 (항상 True)
         */
        return mockFastApiService.missionAuthentication(requestEntity);
    }

    private Object convertMultipartFileToResource(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }

    /**
     * 파일명이 겹치는 것을 방지하기위해 중복되지않는 UUID를 생성해서 반환(ext는 확장자)
     */
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * 파일 확장자를 추출하기 위해 만든 메서드
     */
    private String extractExt(String originalFilename) {
        int post = originalFilename.lastIndexOf(".");
        return originalFilename.substring(post + 1);
    }

    /**
     * S3 에 저장한 후 Fast API 에 호출
     * 후순위
     */
    public MissionAuthenticate authMissionAfterSavedS3(Long missionHistoryId, Long userId ,
                                                       MultipartFile imageFile) throws IOException {
        User user = userRepository
                .findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        MissionHistory missionHistory = missionHistoryRepository.findById(missionHistoryId);

        //파일의 원본 이름
        String originalFileName = imageFile.getOriginalFilename();
        //DB에 저장될 파일 이름
        String storeFileName = createStoreFileName(originalFileName);
        //S3에 저장
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());
        amazonS3Client.putObject(bucket, storeFileName, imageFile.getInputStream(), metadata);

        return MissionAuthenticate.builder().build();
    }
}
