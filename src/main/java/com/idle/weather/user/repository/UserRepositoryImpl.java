package com.idle.weather.user.repository;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.api.response.LevelResponseDto;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.idle.weather.level.api.response.LevelResponseDto.*;
import static java.util.stream.Collectors.*;

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
    public List<User> findTop10ByOrderByLevelDescExperienceDesc() {
        return userJpaRepository.findTop10ByOrderByLevelDescPointDesc().stream()
                .map(UserEntity::toDomain).collect(toList());
    }

    @Override
    public Page<User> findAllByOrderByLevelDescPointDesc(Pageable pageable) {
        return userJpaRepository.findAllByOrderByLevelDescPointDesc(pageable)
                        .map(UserEntity::toDomain);
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
    public User findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();
    }

    @Override
    public SingleRanking findByRankFromNickname(String nickName) {
        User user = userJpaRepository.findByNickname(nickName)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).toDomain();
        int rank = userJpaRepository.calculateUserRanking(user.getLevel(), user.getPoint());
        return SingleRanking.of(user,rank);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.toEntity(user)).toDomain();
    }

    @Override
    public Optional<UserEntity> findByIdForLegacy(Long id) {
        return userJpaRepository.findById(id);
    }
}
