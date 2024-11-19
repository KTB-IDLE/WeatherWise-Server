package com.idle.weather.user.repository;

import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT u.id as id, u.password as password, u.role as role" +
            " FROM UserEntity u WHERE u.serialId = :userId")
    Optional<UserSecurityForm> findUserIdAndRoleBySerialId(@Param("userId") String userId);

    Optional<UserSecurityForm> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long id, boolean b);

    Optional<UserSecurityForm> findBySerialIdAndProvider(String serialId, EProvider provider);

    @Modifying(clearAutomatically = true)
    @Query(value = "update UserEntity u set u.refreshToken = :refreshToken, u.isLogin = :isLogin where u.id = :userId")
    void updateRefreshTokenAndLoginStatus(@Param("userId") Long userId, @Param("refreshToken") String refreshToken, @Param("isLogin") Boolean isLogin);

    // 레벨 기준으로 상위 10명의 User 를 가지고 오는 쿼리 (레벨이 같은 경우에는 Point 를 비교)
    List<UserEntity> findTop10ByOrderByLevelDescPointDesc();

    @Query("SELECT u FROM UserEntity u ORDER BY u.level DESC, u.point DESC")
    Page<UserEntity> findAllByOrderByLevelDescPointDesc(Pageable pageable);

    // 자신의 랭킹을 구하는 쿼리
    @Query("SELECT COUNT(u) + 1 FROM UserEntity u WHERE u.level > :level")
    int findUserRanking(@Param("level") int level);

    @Query("SELECT u.isCompletedSurvey FROM UserEntity u WHERE u.id = :userId")
    boolean checkSurvey(@Param("userId") Long userId);

    interface UserSecurityForm {
        static UserSecurityForm invoke(UserEntity user) {
            return new UserSecurityForm() {
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

    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.serialId = :serialId
            AND u.isDeleted = :isDeleted
            """)
    Optional<UserEntity> findBySerialIdAndIsDeleted(@Param("serialId") String serialId, @Param("isDeleted") boolean isDeleted);

}
