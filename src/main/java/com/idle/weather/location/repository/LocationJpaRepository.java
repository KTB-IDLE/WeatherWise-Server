package com.idle.weather.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationJpaRepository extends JpaRepository<LocationEntity , Long> {
}
