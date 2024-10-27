package com.idle.weather.mission.repository;

import com.idle.weather.global.BaseEntity;
import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import com.idle.weather.missionhistory.repository.MissionHistoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.*;

@Entity
@Getter
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "missions") @Builder
public class MissionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;
    private String name;
    private int point;

    private String uploadFileName;
    private String storeFileName;

    @OneToMany(mappedBy = "mission")
    private List<MissionHistoryEntity> missionHistories = new ArrayList<>();

    @Enumerated(STRING)
    private MissionType missionType;

    public Mission toDomain() {
        return Mission.builder()
                .id(id)
                .point(point)
                .name(name)
                .missionType(missionType)
                .storeFileName(storeFileName)
                .uploadFileName(uploadFileName)
                .build();
    }
}
