package com.idle.weather.level.api.response;

import com.example.mission.User;
import com.idle.weather.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class LevelResponseDto {

    @Builder @Getter
    public static class RankingList {
        private List<SingleRanking> rankingList;
        private int currentUserRanking;
        private String currentUserNickName;
        private int currentUserLevel;

        public static RankingList of(List<SingleRanking> rankingList,
                                     int currentUserRanking , String currentUserNickName , int currentUserLevel) {
            return RankingList.builder()
                    .rankingList(rankingList)
                    .currentUserRanking(currentUserRanking)
                    .currentUserNickName(currentUserNickName)
                    .currentUserLevel(currentUserLevel)
                    .build();
        }
    }

    @Builder @Getter
    public static class SingleRanking {
        private String nickName;
        private int level;
        public static SingleRanking from(User user) {
            return SingleRanking.builder()
                    .level(user.getLevel())
                    .nickName(user.getNickname())
                    .build();
        }
    }
}
