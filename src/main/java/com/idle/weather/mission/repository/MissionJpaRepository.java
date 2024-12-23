package com.idle.weather.mission.repository;

import com.idle.weather.mission.domain.Mission;
import com.idle.weather.mission.domain.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionJpaRepository extends JpaRepository<MissionEntity , Long> {
    List<MissionEntity> findByMissionType(MissionType missionType);
    @Query("SELECT m FROM MissionEntity m WHERE m.id NOT IN (:excludeIds) AND m.missionType IN :types")
    List<MissionEntity> findMissionsExcludingWithTypes(@Param("excludeIds") List<Long> excludeIds, @Param("types") List<MissionType> types);
}
