package com.idle.weather.user.service;

import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.api.response.UserResponse;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j; // 로그를 위한 추가

import java.util.Optional;

@Slf4j // 로그 어노테이션 추가
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse signUp(AuthSignUpDto authSignUpDto) {
        String encodedPass = passwordEncoder.encode(authSignUpDto.password());
        UserEntity newUser = UserEntity.signUp(authSignUpDto, encodedPass);
        return UserResponse.from(userJpaRepository.save(newUser));
    }

    @Override
    public UserResponse findById(Long userId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserResponse.from(user);
    }

    @Override
    public UserResponse findBySerialId(String serialId) {
        UserEntity user = userJpaRepository.findUserIdAndRoleBySerialId(serialId)
                .flatMap(userSecurityForm -> userJpaRepository.findById(userSecurityForm.getId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);
        userJpaRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse updateNickname(Long userId, String newNickname) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.updateInfo(newNickname);
        return UserResponse.from(userJpaRepository.save(user));
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken, boolean isLogin) {
        userJpaRepository.updateRefreshTokenAndLoginStatus(userId, refreshToken, isLogin);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.delete();
        userJpaRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse reActivateUser(String serialId, String newPassword) {
        UserEntity user = userJpaRepository.findBySerialIdAndIsDeleted(serialId, true)
                .orElseThrow(() -> new IllegalArgumentException("User not found or already active"));
        user.reactivate(passwordEncoder.encode(newPassword));
        return UserResponse.from(userJpaRepository.save(user));
    }
}