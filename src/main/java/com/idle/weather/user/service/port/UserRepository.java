package com.idle.weather.user.service.port;

import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserJpaRepository.UserSecurityForm> findUserIdAndRoleBySerialId(@Param("userId") String userId);

    Optional<UserJpaRepository.UserSecurityForm> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long id, boolean b);

    Optional<UserJpaRepository.UserSecurityForm> findBySerialIdAndProvider(String serialId, EProvider provider);

    void updateRefreshTokenAndLoginStatus(@Param("userId") Long userId, @Param("refreshToken") String refreshToken, @Param("isLogin") Boolean isLogin);

    // 레벨 기준으로 상위 10명의 User 를 가지고 오는 쿼리
    List<User> findTop10ByOrderByLevelDescExperienceDesc();
    // 페이징 쿼리
    Page<User> findAllByOrderByLevelDescPointDesc(Pageable pageable);
    int findUserRanking(@Param("level") int level);
    boolean checkSurvey(@Param("userId") Long userId);
    Optional<UserEntity> findBySerialIdAndIsDeleted(@Param("serialId") String serialId, @Param("isDeleted") boolean isDeleted);

    User findById(Long id);
    Optional<UserEntity> findByIdForLegacy(Long id);

    User save(User user);
}
