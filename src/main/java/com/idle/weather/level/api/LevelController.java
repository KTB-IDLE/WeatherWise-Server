package com.idle.weather.level.api;

import com.idle.weather.common.annotation.UserId;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.level.api.port.LevelService;
import com.idle.weather.level.api.response.ExpByLevelResponse;
import com.idle.weather.level.api.response.LevelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LevelController {

    private final LevelService levelService;

    @GetMapping("/ranking")
    public ResponseEntity<BaseResponse<LevelResponseDto.RankingList>> getRanking(@UserId Long userId) {
        return ResponseEntity.ok().body(new BaseResponse<>(levelService.getRankingList(userId)));
    }

    @GetMapping("/exp")
    public ResponseEntity<BaseResponse<ExpByLevelResponse>> getExpByLevel(@RequestParam int level) {
        return ResponseEntity.ok().body(new BaseResponse<>(levelService.getExpByLevel(level)));
    }
}
