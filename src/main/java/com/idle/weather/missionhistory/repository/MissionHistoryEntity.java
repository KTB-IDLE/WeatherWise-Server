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
import static jakarta.persistence.FetchType.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name ="mission_id")
    private MissionEntity mission;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @Enumerated(STRING)
    private MissionTime missionTime;
    private String uploadFileName;
    private String storeFileName;

    public static MissionHistoryEntity toEntity(MissionHistory missionHistory) {
        return MissionHistoryEntity.builder()
                .id(missionHistory.getId())
                .isCompleted(missionHistory.isCompleted())
                .mission(MissionEntity.toEntity(missionHistory.getMission()))
                .missionTime(missionHistory.getMissionTime())
                .storeFileName(missionHistory.getStoreFileName())
                .uploadFileName(missionHistory.getUploadFileName())
                .user(UserEntity.toEntity(missionHistory.getUser()))
                .isCompleted(missionHistory.isCompleted())
                .completedAt(missionHistory.getCompletedAt())
                .build();
    }

    public MissionHistory toDomain() {
        return MissionHistory.builder()
                .id(id)
                .mission(mission.toDomain())
                .isCompleted(isCompleted)
                .missionTime(missionTime)
                .storeFileName(storeFileName)
                .uploadFileName(uploadFileName)
                .user(user.toDomain())
                .isCompleted(isCompleted)
                .completedAt(completedAt)
                .build();
    }
}

