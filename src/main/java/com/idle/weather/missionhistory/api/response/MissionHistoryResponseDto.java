package com.idle.weather.missionhistory.api.response;

import com.idle.weather.missionhistory.domain.MissionHistory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MissionHistoryResponseDto {

    @Builder @Getter
    public static class SingleMissionHistory {
        private Long id;
        private String name;
        private boolean isCompleted;
        private int point;

        public static SingleMissionHistory from(MissionHistory mh) {
            return SingleMissionHistory.builder()
                    .id(mh.getId())
                    .point(mh.getMission().getPoint())
                    .isCompleted(mh.isCompleted())
                    .name(mh.getMission().getName())
                    .build();
        }
    }

    @Builder @Getter
    public static class MissionHistoriesInfo {
        private String nickName;
        private List<SingleMissionHistory> missionList;

        public static MissionHistoriesInfo of(String nickName , List<SingleMissionHistory> missionList) {
            return MissionHistoriesInfo.builder()
                    .nickName(nickName)
                    .missionList(missionList)
                    .build();
        }
    }

    @Builder @Getter
    public static class MissionAuthenticationView {
        private String nickName;
        private String missionName;
        private boolean isCompleted;
        private String uploadFileLink;

        public static MissionAuthenticationView of(String nickName , MissionHistory missionHistory) {
            return MissionAuthenticationView.builder()
                    .missionName(missionHistory.getMission().getName())
                    .nickName(nickName)
                    .isCompleted(missionHistory.isCompleted())
                    .uploadFileLink(missionHistory.getUploadFileLink())
                    .build();
        }
    }

    @Builder @Getter
    public static class MissionAuthenticate {
        private boolean isAuthenticated;
        private int missionExp; // 해당 미션 포인트
        private int userLevel; // 현재 user level
        private int userExp; // 현재 user 가 보여하고 있는 포인트
        private int userLevelTotalExp; // 현재 user level 의 총 경험치

        public static MissionAuthenticate of(boolean isAuthenticated , int missionPoint , int userLevel ,
                                             int userPoint , int userLevelTotalExp) {
            return MissionAuthenticate.builder()
                    .isAuthenticated(isAuthenticated)
                    .missionExp(missionPoint)
                    .userLevel(userLevel)
                    .userExp(userPoint)
                    .userLevelTotalExp(userLevelTotalExp)
                    .build();
        }

        public static MissionAuthenticate fail() {
            return MissionAuthenticate.builder()
                    .isAuthenticated(false)
                    .build();
        }
    }
    @Builder @Getter
    public static class SuccessMissionHistories {
        private List<SingleMissionHistory> missionList;

        public static SuccessMissionHistories from(List<SingleMissionHistory> missionList) {
            return SuccessMissionHistories.builder()
                    .missionList(missionList)
                    .build();
        }

    }


}
