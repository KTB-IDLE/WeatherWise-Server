package com.idle.weather.user.repository;

import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserJpaRepository.UserSecurityForm> findUserIdAndRoleBySerialId(String userId) {
        return userJpaRepository.findUserIdAndRoleBySerialId(userId);
    }

    @Override
    public Optional<UserJpaRepository.UserSecurityForm> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long id, boolean b) {
        return userJpaRepository.findByIdAndIsLoginAndRefreshTokenIsNotNull(id,b);
    }

    @Override
    public Optional<UserJpaRepository.UserSecurityForm> findBySerialIdAndProvider(String serialId, EProvider provider) {
        return userJpaRepository.findBySerialIdAndProvider(serialId,provider);
    }

    @Override
    public void updateRefreshTokenAndLoginStatus(Long userId, String refreshToken, Boolean isLogin) {
        userJpaRepository.updateRefreshTokenAndLoginStatus(userId,refreshToken,isLogin);

    }

    @Override
    public List<UserEntity> findTop10ByOrderByLevelDesc() {
        return userJpaRepository.findTop10ByOrderByLevelDesc();
    }

    @Override
    public int findUserRanking(int level) {
        return userJpaRepository.findUserRanking(level);
    }

    @Override
    public boolean checkSurvey(Long userId) {
        return userJpaRepository.checkSurvey(userId);
    }

    @Override
    public Optional<UserEntity> findBySerialIdAndIsDeleted(String serialId, boolean isDeleted) {
        return userJpaRepository.findBySerialIdAndIsDeleted(serialId,isDeleted);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }
}
