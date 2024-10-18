package com.idle.weather.missionhistory.api;

import com.idle.weather.missionhistory.api.port.MissionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MissionHistoryController {
    private final MissionHistoryService missionHistoryService;
}
