//package com.idle.weather.level.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface LevelJpaRepository extends JpaRepository<LevelEntity , Integer> {
//    @Query("SELECT l FROM LevelEntity l WHERE l.level = :level")
//    LevelEntity findByLevel(@Param("level") int level);
//}
