package com.idle.weather.mock;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;


import java.util.*;
import java.util.stream.Collectors;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeUserRepository implements UserRepository {
    private static Long id = 0L;
    private final List<User> data = new ArrayList<>();

    @Override
    public User findById(Long id) {
        return data.stream().filter(item -> item.getId().equals(id)).findAny()
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            User newUser = User.builder()
                    .id(id++)
                    .serialId(user.getSerialId())
                    .nickname(user.getNickname())
                    .role(user.getRole())
                    .password(user.getPassword())
                    .missionHistories(new ArrayList<>())
                    .provider(user.getProvider())
                    .level(user.getLevel())
                    .point(user.getPoint())
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            // 같은 User 라면 기존에 것을 삭제하고 새로 추가
            data.removeIf(item -> Objects.equals(item.getId() , user.getId()));
            data.add(user);
            return user;
        }
    }

    @Override
    public List<User> findTop10ByOrderByLevelDesc() {
        return data.stream()
                .sorted(Comparator.comparingInt(User::getLevel).reversed()) // level을 기준으로 내림차순 정렬
                .limit(10) // 상위 10명 선택
                .collect(Collectors.toList());
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



    @Override
    public Optional<UserEntity> findByIdForLegacy(Long id) {
        return Optional.empty();
    }

    public void clear() {
        data.clear();
    }

    public int getDataSize() {
        return data.size();
    }
}
