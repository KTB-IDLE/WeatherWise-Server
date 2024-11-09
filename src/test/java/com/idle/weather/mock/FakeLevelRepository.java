package com.idle.weather.mock;

import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.level.domain.Level;
import com.idle.weather.level.service.port.LevelRepository;
import com.idle.weather.mission.domain.Mission;

import java.util.ArrayList;
import java.util.List;

public class FakeLevelRepository implements LevelRepository {
    private final List<Level> data = new ArrayList<>();
    public FakeLevelRepository() {
        Level level_1 = Level.builder()
                .level(1)
                .minExp(0)
                .maxExp(100)
                .build();
        data.add(level_1);

        Level level_2 = Level.builder()
                .level(2)
                .minExp(101)
                .maxExp(200)
                .build();
        data.add(level_2);

        Level level_3 = Level.builder()
                .level(3)
                .minExp(201)
                .maxExp(300)
                .build();
        data.add(level_3);
    }

    @Override
    public Level findById(int level) {
        return data.stream().filter(l -> l.getLevel() == level)
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LEVEL));
    }
}
