package com.idle.weather.location.repository;

import com.idle.weather.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {
    Optional<LocationEntity> findByLocationName(String locationName);
}
