package com.idle.weather.level.api;

import com.idle.weather.level.api.port.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LevelController {
    private final LevelService levelService;
}
