package com.idle.weather.level.service.port;

import com.idle.weather.level.domain.Level;

public interface LevelRepository {
    Level findById(int level);
}
