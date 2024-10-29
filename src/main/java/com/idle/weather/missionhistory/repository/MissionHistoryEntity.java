package com.idle.weather.missionhistory.repository;

import com.idle.weather.global.BaseEntity;
import com.idle.weather.mission.repository.MissionEntity;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_histories") @Builder
public class MissionHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_history_id")
    private Long id;

    private LocalDateTime completedAt;
    private boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="mission_id")
    private MissionEntity mission;

    @Enumerated(STRING)
    private MissionTime missionTime;
    private String uploadFileName;
    private String storeFileName;

    public static MissionHistoryEntity toEntity(MissionHistory missionHistory) {
        return MissionHistoryEntity.builder()
                .id(missionHistory.getId())
                .isCompleted(missionHistory.isCompleted())
                .mission(MissionEntity.toEntity(missionHistory.getMission()))
                .user(UserEntity.toEntity(missionHistory.getUser()))
                .missionTime(missionHistory.getMissionTime())
                .build();
    }

    public MissionHistory toDomain() {
        return MissionHistory.builder()
                .id(id)
                .mission(mission.toDomain())
                .isCompleted(isCompleted)
                .build();
    }

    public void updateCompleted(MissionHistory missionHistory) {
        this.isCompleted = missionHistory.isCompleted();
    }
}

