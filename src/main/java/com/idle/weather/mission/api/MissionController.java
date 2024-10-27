package com.idle.weather.mission.api;

import com.idle.weather.common.annotation.UserId;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.mission.api.port.MissionService;
import com.idle.weather.mission.api.request.MissionRequestDto;
import com.idle.weather.mission.domain.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.idle.weather.mission.api.request.MissionRequestDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MissionController {
    private final MissionService missionService;

    // 미션 생성
    @PostMapping("/missions")
    public ResponseEntity<BaseResponse<Mission>> createMission(@UserId Long userId,
                                                               @RequestBody CreateMission createMission) {
        return ResponseEntity.ok().body(new BaseResponse<>(missionService
                .createMission(userId,createMission.getNx(),createMission.getNy())));
    }
}
