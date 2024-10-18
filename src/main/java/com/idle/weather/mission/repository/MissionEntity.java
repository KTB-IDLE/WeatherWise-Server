package com.idle.weather.mission.repository;

import com.idle.weather.global.BaseEntity;
import com.idle.weather.mission.domain.Mission;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "missions")
public class MissionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;
    private String name;
    private int point;

    @OneToMany(mappedBy = "mission")
    private List<MissionHistoryEntity> missionHistories = new ArrayList<>();

    public Mission toDomain() {
        return Mission.builder()
                .id(id)
                .point(point)
                .name(name)
                .build();
    }
}
