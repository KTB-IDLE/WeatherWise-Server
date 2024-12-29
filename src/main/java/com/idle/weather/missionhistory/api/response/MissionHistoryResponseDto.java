package com.idle.weather.missionhistory.api.response;

import com.idle.weather.missionhistory.domain.MissionHistory;
import com.idle.weather.missionhistory.repository.MissionTime;

import java.util.List;

public class MissionHistoryResponseDto {

    public record SingleMissionHistory(
            Long id,
            String name,
            boolean isCompleted,
            MissionTime missionTime,
            String description,
            int point
    ) {
        public static SingleMissionHistory from(MissionHistory mh) {
            return new SingleMissionHistory(
                    mh.getId(),
                    mh.getMission().getName(),
                    mh.isCompleted(),
                    mh.getMissionTime(),
                    mh.getMission().getDescription(),
                    mh.getMission().getPoint()
            );
        }
    }

    public record MissionHistoriesInfo(
            String nickName,
            List<SingleMissionHistory> missionList
    ) {
        public static MissionHistoriesInfo of(String nickName, List<SingleMissionHistory> missionList) {
            return new MissionHistoriesInfo(nickName, missionList);
        }
    }

    public record MissionAuthenticationView(
            String nickName,
            String missionName,
            String missionDescription,
            boolean isCompleted,
            String uploadFileLink,
            String storeFileName
    ) {
        public static MissionAuthenticationView of(String nickName, MissionHistory missionHistory) {
            return new MissionAuthenticationView(
                    nickName,
                    missionHistory.getMission().getName(),
                    missionHistory.getMission().getDescription(),
                    missionHistory.isCompleted(),
                    missionHistory.getUploadFileName(),
                    missionHistory.getStoreFileName()
            );
        }
    }

    public record MissionAuthenticate(
            boolean isAuthenticated,
            int missionExp,
            int userLevel,
            int userExp
    ) {
        public static MissionAuthenticate of(boolean isAuthenticated, int missionPoint, int userLevel, int userPoint) {
            return new MissionAuthenticate(isAuthenticated, missionPoint, userLevel, userPoint);
        }

        public static MissionAuthenticate fail() {
            return new MissionAuthenticate(false, 0, 0, 0);
        }
    }

    public record SuccessMissionHistories(
            List<SingleMissionHistory> missionList
    ) {
        public static SuccessMissionHistories from(List<SingleMissionHistory> missionList) {
            return new SuccessMissionHistories(missionList);
        }
    }
}