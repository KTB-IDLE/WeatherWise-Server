package com.idle.weather.user.api.port;

import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;

import java.util.Optional;

public interface UserService {
    User signUp(AuthSignUpDto authSignUpDto);
    Optional<User> findById(Long id);
    Optional<User> findBySerialId(String serialId);
    void updateRefreshToken(Long userId, String refreshToken, boolean isLogin);
}
