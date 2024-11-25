package com.idle.weather.level.api.response;

import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
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
        private boolean isExistUserInCurrentPage;
        private boolean hasNext;
        private boolean hasPrevious;

        public static RankingList of(List<SingleRanking> rankingList,
                                     int currentUserRanking , String currentUserNickName , int currentUserLevel,
                                     boolean hasNext , boolean hasPrevious , boolean isExistUserInCurrentPage) {
            return RankingList.builder()
                    .rankingList(rankingList)
                    .currentUserRanking(currentUserRanking)
                    .currentUserNickName(currentUserNickName)
                    .currentUserLevel(currentUserLevel)
                    .hasNext(hasNext)
                    .hasPrevious(hasPrevious)
                    .isExistUserInCurrentPage(isExistUserInCurrentPage)
                    .build();
        }
    }

    @Builder @Getter
    public static class SingleRanking {
        private String nickName;
        private int level;
        private int rank;
        public static SingleRanking of(User user , int rank) {
            return SingleRanking.builder()
                    .level(user.getLevel())
                    .nickName(user.getNickname())
                    .rank(rank)
                    .build();
        }

        public static SingleRanking from(User user) {
            return SingleRanking.builder()
                    .level(user.getLevel())
                    .nickName(user.getNickname())
                    .build();
        }
    }
}
