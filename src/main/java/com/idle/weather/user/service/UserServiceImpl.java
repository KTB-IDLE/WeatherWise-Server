package com.idle.weather.user.service;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.api.request.SurveyRequestDto;
import com.idle.weather.user.api.response.UserInfoResponse;
import com.idle.weather.user.api.response.UserResponse;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.dto.SurveyDto;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j; // 로그를 위한 추가


@Slf4j // 로그 어노테이션 추가
@Service @Builder
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public UserResponse signUp(AuthSignUpDto authSignUpDto) {
        userJpaRepository.findByNicknameAndIsDeleted(authSignUpDto.nickname(), false)
        .ifPresent(u -> {
            throw new BaseException(ErrorCode.DUPLICATION_NICKNAME);
        });

        String encodedPass = passwordEncoder.encode(authSignUpDto.password());
        UserEntity newUser = UserEntity.signUp(authSignUpDto, encodedPass);
        User user = userRepository.save(newUser.toDomain());
        return UserResponse.from(UserEntity.toEntity(user));
    }

    @Override
    public UserInfoResponse findById(Long userId) {
        User user = userRepository.findById(userId);
        return UserInfoResponse.from(UserEntity.toEntity(user));
    }

    @Override
    public UserResponse findBySerialId(String serialId) {
        UserEntity user = userRepository.findUserIdAndRoleBySerialId(serialId)
                .flatMap(userSecurityForm -> userRepository.findByIdForLegacy(userSecurityForm.getId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId);
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse updateNickname(Long userId, String newNickname) {
        userJpaRepository.findByNicknameAndIsDeleted(newNickname, false)
        .ifPresent(u -> {
            throw new BaseException(ErrorCode.DUPLICATION_NICKNAME);
        });
        
        User user = userRepository.findById(userId);
        user.updateInfo(newNickname);
        return UserResponse.from(UserEntity.toEntity(userRepository.save(user)));
    }

    @Override
    public String getNickname(Long userId) {
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
        log.info("getNickName = {} " , user.getNickname());
        return user.getNickname();
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken, boolean isLogin) {
        userRepository.updateRefreshTokenAndLoginStatus(userId, refreshToken, isLogin);
    }

    @Override
    public boolean checkSurvey(Long userId) {
        return userRepository.checkSurvey(userId);
    }

    @Override
    @Transactional
    public void applySurveyResult(Long userId, SurveyRequestDto surveyResult) {
        User user = userRepository.findById(userId);
        user.applySurveyResult(surveyResult);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId);
        user.delete();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse reActivateUser(String serialId, String newPassword) {
        UserEntity user = userRepository.findBySerialIdAndIsDeleted(serialId, true)
                .orElseThrow(() -> new IllegalArgumentException("User not found or already active"));
        user.reactivate(passwordEncoder.encode(newPassword));
        return UserResponse.from(UserEntity.toEntity(userRepository.save(user.toDomain())));
    }
}