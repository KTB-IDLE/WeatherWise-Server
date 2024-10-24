package com.idle.weather.user.service;

import com.idle.weather.user.api.port.UserService;
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
    public UserEntity signUp(AuthSignUpDto authSignUpDto) {
        String encodedPass = passwordEncoder.encode(authSignUpDto.password());
        UserEntity newUser = UserEntity.signUp(authSignUpDto, encodedPass);

        return userJpaRepository.save(newUser);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findBySerialId(String serialId) {
        return userJpaRepository.findUserIdAndRoleBySerialId(serialId)
                .flatMap(userSecurityForm -> userJpaRepository.findById(userSecurityForm.getId()));
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken, boolean isLogin) {
        userJpaRepository.updateRefreshTokenAndLoginStatus(userId, refreshToken, isLogin);
    }
}