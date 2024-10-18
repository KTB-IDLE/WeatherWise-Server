package com.idle.weather.missionhistory.repository;

import com.idle.weather.global.BaseEntity;
import com.idle.weather.mission.repository.MissionEntity;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_histories")
public class MissionHistoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_history_id")
    private Long id;

    private LocalDateTime completedAt;
    private boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="mission_id")
    private MissionEntity mission;

    private String uploadFileName;
    private String storeFileName;

    public MissionHistory toDomain() {
        return MissionHistory.builder()
                .id(id)
                .mission(mission.toDomain())
                .isCompleted(isCompleted)
                .uploadFileLink(uploadFileName)
                .build();
    }

    public void updateCompleted(MissionHistory missionHistory) {
        this.isCompleted = missionHistory.isCompleted();
    }
}

