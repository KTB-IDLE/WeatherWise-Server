package com.idle.weather.user.service;

import com.idle.weather.user.api.port.UserService;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
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
    public User signUp(AuthSignUpDto authSignUpDto) {
        log.info("SignUp process initiated for serialId: {}", authSignUpDto.serialId());

        String encodedPass = passwordEncoder.encode(authSignUpDto.password());
        User newUser = User.signUp(authSignUpDto, encodedPass);

        log.info("User saved successfully: serialId = {}", newUser.getSerialId());
        return userJpaRepository.save(newUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<User> findBySerialId(String serialId) {
        return userJpaRepository.findUserIdAndRoleBySerialId(serialId)
                .flatMap(userSecurityForm -> userJpaRepository.findById(userSecurityForm.getId()));
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken, boolean isLogin) {
        userJpaRepository.updateRefreshTokenAndLoginStatus(userId, refreshToken, isLogin);
    }
}