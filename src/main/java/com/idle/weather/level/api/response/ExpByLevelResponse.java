//package com.idle.weather.level.api.response;
//
//import com.idle.weather.boardvote.api.response.BoardVoteResponse;
//import com.idle.weather.boardvote.domain.BoardVote;
//import com.idle.weather.level.repository.LevelEntity;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//public record ExpByLevelResponse(
//    @Schema(description = "레벨", example = "2")
//    int level,
//
//    @Schema(description = "포인트 시작점", example = "101")
//    int minExp,
//
//    @Schema(description = "포인트 종료점", example = "200")
//    int maxExp
//) {
//    public static ExpByLevelResponse from(LevelEntity level) {
//        return new ExpByLevelResponse(
//                level.getLevel(),
//                level.getMinExp(),
//                level.getMaxExp()
//        );
//    }
//}
