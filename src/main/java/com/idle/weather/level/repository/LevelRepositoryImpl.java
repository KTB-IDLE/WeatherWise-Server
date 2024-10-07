package com.idle.weather.level.repository;

import com.idle.weather.level.service.port.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LevelRepositoryImpl implements LevelRepository {
    private final LevelJpaRepository levelJpaRepository;
}
