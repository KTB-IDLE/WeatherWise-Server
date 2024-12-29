package com.idle.weather.level.api.response;

import com.idle.weather.user.domain.User;

import java.util.List;

public record LevelResponseDto() {

    public record RankingList(
            List<SingleRanking> rankingList,
            int currentUserRanking,
            String currentUserNickName,
            int currentUserLevel,
            boolean isExistUserInCurrentPage,
            boolean hasNext,
            boolean hasPrevious
    ) {
        public static RankingList of(
                List<SingleRanking> rankingList,
                int currentUserRanking,
                String currentUserNickName,
                int currentUserLevel,
                boolean hasNext,
                boolean hasPrevious,
                boolean isExistUserInCurrentPage
        ) {
            return new RankingList(
                    rankingList,
                    currentUserRanking,
                    currentUserNickName,
                    currentUserLevel,
                    isExistUserInCurrentPage,
                    hasNext,
                    hasPrevious
            );
        }
    }

    public record SingleRanking(
            String nickName,
            int level,
            int rank
    ) {
        public static SingleRanking of(User user, int rank) {
            return new SingleRanking(
                    user.getNickname(),
                    user.getLevel(),
                    rank
            );
        }

        public static SingleRanking from(User user) {
            return new SingleRanking(
                    user.getNickname(),
                    user.getLevel(),
                    0 // 기본 rank 값 (미지정)
            );
        }
    }
}