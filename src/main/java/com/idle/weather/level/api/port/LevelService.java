package com.idle.weather.level.api.port;

import com.idle.weather.level.api.response.LevelResponseDto;

import static com.idle.weather.level.api.response.LevelResponseDto.*;

public interface LevelService {
    RankingList getRankingList(Long userId);
}
