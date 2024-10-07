package com.idle.weather.location.repository;

import com.idle.weather.location.service.port.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {
    private final LocationJpaRepository locationJpaRepository;
}
