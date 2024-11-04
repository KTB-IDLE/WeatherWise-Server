package com.idle.weather.user.service.port;

import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserJpaRepository.UserSecurityForm> findUserIdAndRoleBySerialId(@Param("userId") String userId);

    Optional<UserJpaRepository.UserSecurityForm> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long id, boolean b);

    Optional<UserJpaRepository.UserSecurityForm> findBySerialIdAndProvider(String serialId, EProvider provider);

    void updateRefreshTokenAndLoginStatus(@Param("userId") Long userId, @Param("refreshToken") String refreshToken, @Param("isLogin") Boolean isLogin);

    // 레벨 기준으로 상위 10명의 User 를 가지고 오는 쿼리
    List<UserEntity> findTop10ByOrderByLevelDesc();
    int findUserRanking(@Param("level") int level);
    boolean checkSurvey(@Param("userId") Long userId);
    interface UserSecurityForm {
        static UserJpaRepository.UserSecurityForm invoke(UserEntity user) {
            return new UserJpaRepository.UserSecurityForm() {
                @Override
                public Long getId() {
                    return user.getId();
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public ERole getRole() {
                    return user.getRole();
                }
            };
        }

        Long getId();

        String getPassword();

        ERole getRole();
    }
    Optional<UserEntity> findBySerialIdAndIsDeleted(@Param("serialId") String serialId, @Param("isDeleted") boolean isDeleted);

    Optional<UserEntity> findById(Long id);

    UserEntity save(UserEntity user);
}
