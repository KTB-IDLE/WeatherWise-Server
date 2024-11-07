package com.idle.weather.level.repository;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.domain.Level;
import com.idle.weather.level.service.port.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LevelRepositoryImpl implements LevelRepository {
    private final LevelJpaRepository levelJpaRepository;

    @Override
    public Level findById(int level) {
        return levelJpaRepository.findById(level)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LEVEL)).toDomain();
    }
}
