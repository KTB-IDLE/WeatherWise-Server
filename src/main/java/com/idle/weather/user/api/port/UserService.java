package com.idle.weather.user.api.port;

import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.repository.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity signUp(AuthSignUpDto authSignUpDto);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findBySerialId(String serialId);
    void updateRefreshToken(Long userId, String refreshToken, boolean isLogin);
}
