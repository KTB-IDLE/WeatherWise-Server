package com.idle.weather.user.repository;

import com.idle.weather.global.BaseEntity;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.user.domain.SurveyResult;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.dto.SurveyDto;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@ToString
@Entity
@Getter
@DynamicUpdate
@Table(name = "users") @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "social_id", nullable = false, unique = true)
    private String serialId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private EProvider provider;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(name = "nickname", length = 12)
    private String nickname;

    /* User Status */
    @Column(name = "is_login", nullable = false)
    private Boolean isLogin;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "level" , nullable = false)
    private int level;

    @Column(name = "point" , nullable = false)
    private int point;
    @Embedded
    private SurveyResult surveyResult;
    @Column(name = "is_completed_survey")
    private Boolean isCompletedSurvey;

    @Builder
    public UserEntity(Long id, String serialId, String password, String nickname,
                      EProvider provider, ERole role, boolean easilyHot, boolean easilyCold, boolean easilySweat,
                      Boolean isCompletedSurvey) {
        this.id = id;
        this.serialId = serialId;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.role = role;
        this.isLogin = false;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.isCompletedSurvey = isCompletedSurvey;
        this.level = 1;
        this.point = 0;
    }

    @Builder
    public UserEntity(String serialId, String password, EProvider provider, ERole role , String nickname) {
        this.serialId = serialId;
        this.password = password;
        this.provider = provider;
        this.role = role;
        this.isLogin = true;
        this.nickname = nickname;
        this.level = 1;
        this.point = 0;
        this.isCompletedSurvey = false;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateInfo(String nickname) {
        if (nickname != null && (!Objects.equals(this.nickname, nickname))) {
            this.nickname = nickname;
        }
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public static UserEntity signUp(AuthSignUpDto authSignUpDto, String encodedPassword) {
        UserEntity user = UserEntity.builder()
                .serialId(authSignUpDto.serialId())
                .password(encodedPassword)
                .provider(EProvider.DEFAULT)
                .role(ERole.USER)
                .level(1)
                .point(0)
                .isLogin(Boolean.FALSE)
                // 설문조사 여부
                .isCompletedSurvey(Boolean.FALSE)
                .isDeleted(false)
                .build();
        user.register(authSignUpDto.nickname());
        return user;
    }

    public void register(String nickname) {
        this.nickname = nickname;
        this.role = ERole.USER;
    }

    public static UserEntity toEntity(User  user) {
        return UserEntity.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .level(user.getLevel())
                .point(user.getPoint())
                .password(user.getPassword())
                .provider(user.getProvider())
                .role(user.getRole())
                .serialId(user.getSerialId())
                .isLogin(user.getIsLogin())
                .refreshToken(user.getRefreshToken())
                .surveyResult(user.getSurveyResult())
                .isCompletedSurvey(user.isCompletedSurvey())
                .isDeleted(user.isDeleted())
                .build();
    }
    public User toDomain() {
        return User.builder()
                .id(id)
                .nickname(nickname)
                .password(password)
                .level(level)
                .point(point)
                .role(role)
                .provider(provider)
                .isLogin(isLogin)
                .serialId(serialId)
                .refreshToken(refreshToken)
                .isCompletedSurvey(isCompletedSurvey)
                .isDeleted(isDeleted)
                .missionHistories(new ArrayList<>())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity user)) return false;
        return Objects.nonNull(this.getId()) && Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    public void delete() {
        this.isDeleted = true;
        this.isLogin = false;
        this.refreshToken = null;
    }

    public void reactivate(String newPassword) {
        this.isDeleted = false;
        this.password = newPassword;
        this.isLogin = true;
    }
}

