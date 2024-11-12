package com.idle.weather.missionhistory.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.domain.Level;
import com.idle.weather.level.service.port.LevelRepository;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.api.port.MissionHistoryService;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.domain.SuccessMissionHistory;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.missionhistory.repository.MissionTime;
import com.idle.weather.missionhistory.service.port.AIServerClient;
import com.idle.weather.missionhistory.service.port.MissionHistoryRepository;
import com.idle.weather.mock.MockFastApiService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.service.port.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;
import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j @Builder
public class MissionHistoryServiceImpl implements MissionHistoryService {
    private final MissionHistoryRepository missionHistoryRepository;
    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final AIServerClient aiServerClient;
    // S3
    private final AmazonS3Client amazonS3Client;
    private final String s3Bucket;
    private final String s3DomainName;

    @Override
    public MissionHistoriesInfo getMissionList(LocalDate date , Long userId) {
        User user = userRepository
                .findByIdForLegacy(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        List<MissionHistory> missionHistoryByDate = missionHistoryRepository.findMissionHistoryByDate(user.getId(), date);
        // Domain -> DTO 변환
        List<SingleMissionHistory> result = missionHistoryByDate.stream().map(SingleMissionHistory::from).toList();
        return MissionHistoriesInfo.of(user.getNickname(),result);
    }

    @Override
    public MissionAuthenticationView getMission(Long missionHistoryId , Long userId) {
        User user = userRepository
                .findByIdForLegacy(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();
        MissionHistory missionHistory = missionHistoryRepository.findById(missionHistoryId);
        return MissionAuthenticationView.of(user.getNickname() , missionHistory);
    }

    @Override
    @Transactional
    public MissionHistory save(Long userId , Mission mission, MissionTime missionTime) {
        User user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();

        return missionHistoryRepository.save(MissionHistory.of(user,mission,missionTime));
    }

    @Override
    @Transactional
    public MissionAuthenticate authMission(Long missionHistoryId,
                                           MultipartFile imageFile,
                                           Long userId) throws IOException {
        User user = userRepository.findById(userId);
        MissionHistory missionHistory = missionHistoryRepository.findById(missionHistoryId);

        Mission mission = missionHistory.getMission();

        // S3 에 저장 후 이미지 URL 받아오기
        String imageUrl = getImageUrlAfterSavedS3(imageFile , missionHistory);

        // 이미지 인증 결과 (imageUrl , Mission , User 정보 담아서 AI 서버에 전송)
        boolean authenticationResult  = sendFastAPIServer(MissionAuth.of(imageUrl,mission));

        // 인증 실패시
        if (!authenticationResult) {
            // 실패시 ImageUrl 에 Null 로 지정
            missionHistory.updateImageUrl(null , null);
            return MissionAuthenticate.fail();
        }

        // 인증 성공시
        // User 경험치 추가 (경험치 추가 할 때 레벨 계산)
        Level level = levelRepository.findById(user.getLevel());

        int totalPoint = user.getPoint() + mission.getPoint();
        if (totalPoint >= level.getMaxExp()) {
            user.levelUp(totalPoint-level.getMaxExp());
        }

        user.updatedExperience(mission.getPoint());

        // MissionHistory 인증 성공으로 업데이트
        missionHistory.updateCompleted();

        // 성공
        // User 도메인에 성공한 미션 내역에 추가
        user.getMissionHistories().add(missionHistory);

        // Entity 에 반영하기
        missionHistoryRepository.save(missionHistory);
        userRepository.save(user);

        return MissionAuthenticate.of(authenticationResult , mission.getPoint() ,
                user.getLevel() ,user.getPoint(),level.getMaxExp());
    }

    @Override
    public SuccessMissionHistories getSuccessMissions(Long userId) {
        List<MissionHistory> missionHistoryList = missionHistoryRepository.findMissionHistoriesByUserId(userId);
        List<SingleMissionHistory> userSuccessMissionHistories = missionHistoryList.stream()
                .filter(MissionHistory::isCompleted)
                .map(SingleMissionHistory::from)
                .toList();
        return SuccessMissionHistories.from(userSuccessMissionHistories);
    }


    private String getImageUrlAfterSavedS3(MultipartFile imageFile , MissionHistory missionHistory) throws IOException {
        //파일의 원본 이름
        String originalFileName = imageFile.getOriginalFilename();
        //DB에 저장될 파일 이름
        String storeFileName = createStoreFileName(originalFileName);
        missionHistory.updateImageUrl(originalFileName,s3DomainName + storeFileName);
        //S3에 저장
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());
        amazonS3Client.putObject(s3Bucket, storeFileName, imageFile.getInputStream(), metadata);
        // 업로드된 파일의 URL 가져오기
        return amazonS3Client.getUrl(s3Bucket, storeFileName).toString();
    }

    private boolean sendFastAPIServer(MissionAuth missionAuthDto) throws IOException {
        return aiServerClient.missionAuthentication(missionAuthDto);
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


}
