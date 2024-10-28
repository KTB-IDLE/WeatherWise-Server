package com.idle.weather.missionhistory.api.port;

import com.idle.weather.mission.domain.Mission;
import com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto;
import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import static com.idle.weather.missionhistory.api.response.MissionHistoryResponseDto.*;

public interface MissionHistoryService {
    MissionHistoriesInfo getMissionList(LocalDate date , Long userId);
    MissionAuthenticationView getMission(Long missionHistoryId , Long userId);

    MissionAuthenticate authMission(Long missionHistoryId, MultipartFile imageFile , Long userId) throws IOException;

    SuccessMissionHistories getSuccessMissions(Long userId);
    MissionHistory save(Long userId , Mission mission , MissionTime missionTime);
}
