package com.idle.weather.mock;

import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeUserJpaRepository implements UserRepository {
    private static Long id = 0L;
    private final List<User> data = new ArrayList<>();

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public UserEntity save(UserEntity user) {
        return null;
    }

    @Override
    public Optional<UserJpaRepository.UserSecurityForm> findUserIdAndRoleBySerialId(String userId) {
        return Optional.empty();
    }

    @Override
    public Optional<UserJpaRepository.UserSecurityForm> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long id, boolean b) {
        return Optional.empty();
    }

    @Override
    public Optional<UserJpaRepository.UserSecurityForm> findBySerialIdAndProvider(String serialId, EProvider provider) {
        return Optional.empty();
    }

    @Override
    public void updateRefreshTokenAndLoginStatus(Long userId, String refreshToken, Boolean isLogin) {

    }

    @Override
    public List<UserEntity> findTop10ByOrderByLevelDesc() {
        return null;
    }

    @Override
    public int findUserRanking(int level) {
        return 0;
    }

    @Override
    public boolean checkSurvey(Long userId) {
        return false;
    }

    @Override
    public Optional<UserEntity> findBySerialIdAndIsDeleted(String serialId, boolean isDeleted) {
        return Optional.empty();
    }


}
