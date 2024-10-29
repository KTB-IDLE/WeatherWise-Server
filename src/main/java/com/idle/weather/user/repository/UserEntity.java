package com.idle.weather.user.repository;

import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.AuthSignUpDto;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString
@Entity
@Getter
@DynamicUpdate
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

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

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

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
    // 더위
    @Column(name = "hot" , nullable = false)
    private boolean runHot;
    // 추위
    @Column(name = "cold" , nullable = false)
    private boolean runCold;
    // 땀
    @Column(name = "sweat" , nullable = false)
    private boolean runSweat;

    @OneToMany(mappedBy = "user")
    private List<MissionHistoryEntity> missionHistories = new ArrayList<>();

    @Builder
    public UserEntity(Long id, String serialId, String password, String nickname,
                      EProvider provider, ERole role, boolean runHot , boolean runCold , boolean runSweat) {
        this.id = id;
        this.serialId = serialId;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.role = role;
        this.createdDate = LocalDate.now();
        this.isLogin = false;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.runCold = runCold;
        this.runHot = runHot;
        this.runSweat = runSweat;
        this.level = 1;
        this.point = 0;
    }

    @Builder
    public UserEntity(String serialId, String password, EProvider provider, ERole role) {
        this.serialId = serialId;
        this.password = password;
        this.provider = provider;
        this.role = role;
        this.createdDate = LocalDate.now();
        this.isLogin = true;
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
                .runCold(authSignUpDto.survey().runCold())
                .runSweat(authSignUpDto.survey().runSweat())
                .runHot(authSignUpDto.survey().runHot())
                .build();
        user.register(authSignUpDto.nickname());
        return user;
    }

    public void register(String nickname) {
        this.nickname = nickname;
        this.role = ERole.USER;
    }
    public User toDomain() {
        return User.builder()
                .nickname(nickname)
                .level(level)
                .point(point)
                .missionHistories(missionHistories.stream().map(MissionHistoryEntity::toDomain).collect(Collectors.toList()))
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

