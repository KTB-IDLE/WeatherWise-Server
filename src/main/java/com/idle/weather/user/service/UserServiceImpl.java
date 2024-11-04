package com.idle.weather.user.service;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.api.response.UserResponse;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.dto.SurveyDto;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j; // 로그를 위한 추가


@Slf4j // 로그 어노테이션 추가
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse signUp(AuthSignUpDto authSignUpDto) {
        String encodedPass = passwordEncoder.encode(authSignUpDto.password());
        UserEntity newUser = UserEntity.signUp(authSignUpDto, encodedPass);
        User user = userRepository.save(newUser.toDomain());
        return UserResponse.from(UserEntity.toEntity(user));
    }

    @Override
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId);
        return UserResponse.from(UserEntity.toEntity(user));
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
        User user = userRepository.findById(userId);
        user.updateInfo(newNickname);
        return UserResponse.from(UserEntity.toEntity(userRepository.save(user)));
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
    public void applySurveyResult(Long userId, SurveyDto surveyResult) {
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