package com.idle.weather.missionhistory.api;

import com.idle.weather.global.BaseResponse;
import com.idle.weather.missionhistory.api.port.MissionHistoryService;
import com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MissionHistoryController {
    private final MissionHistoryService missionHistoryService;

    @GetMapping("/mission-histories")
    public ResponseEntity<BaseResponse<MissionHistoriesInfo>> getMissions(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok().body(new BaseResponse<>(missionHistoryService.getMissionList(date)));
    }

    // 미션 수행
    @GetMapping("/mission-histories/{mission-histories-id}")
    public ResponseEntity<BaseResponse<MissionAuthenticationView>> getMission(@PathVariable("mission-histories-id") Long missionHistoryId) {
        return ResponseEntity.ok().body(new BaseResponse<>(missionHistoryService.getMission(missionHistoryId)));
    }

    // 미션 수행
    @PostMapping("/mission-histories/{mission-histories-id}")
    public ResponseEntity<BaseResponse<MissionAuthenticate>> authMission(@PathVariable("mission-histories-id") Long missionHistoryId,
                                                                         @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok().body(new BaseResponse<>(missionHistoryService.authMission(missionHistoryId , imageFile)));
    }

    // 성공한 미션들 조회
    @GetMapping("/mission-histories/success")
    public ResponseEntity<BaseResponse<SuccessMissionHistories>> getSuccessMissions() {
        return ResponseEntity.ok().body(new BaseResponse<>(missionHistoryService.getSuccessMissions()));
    }

}
