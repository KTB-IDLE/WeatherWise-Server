package com.idle.weather.level.api;

import com.idle.weather.common.annotation.UserId;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.level.api.port.LevelService;
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

    @GetMapping("/rankings")
    public ResponseEntity<BaseResponse<LevelResponseDto.RankingList>> getRanking(@UserId Long userId,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(new BaseResponse<>(levelService.getRankingList(userId,page,size)));
    }

    @GetMapping("/ranking")
    public ResponseEntity<BaseResponse<LevelResponseDto.SingleRanking>> getRankingByNickName(@UserId Long userId,
                                                                                           @RequestParam("nickname") String nickName) {
        return ResponseEntity.ok().body(new BaseResponse<>(levelService.getRankingByNickName(nickName)));
    }

    /*@GetMapping("/exp")
    public ResponseEntity<BaseResponse<ExpByLevelResponse>> getExpByLevel(@RequestParam int level) {
        return ResponseEntity.ok().body(new BaseResponse<>(levelService.getExpByLevel(level)));
    }*/
}
