package com.idle.weather.missionhistory.api.port;

import com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;

public interface MissionHistoryService {
    MissionHistoriesInfo getMissionList(LocalDate date , Long userId);
    MissionAuthenticationView getMission(Long missionHistoryId , Long userId);

    MissionAuthenticate authMission(Long missionHistoryId, MultipartFile imageFile , Long userId) throws IOException;

    SuccessMissionHistories getSuccessMissions(Long userId);
}
