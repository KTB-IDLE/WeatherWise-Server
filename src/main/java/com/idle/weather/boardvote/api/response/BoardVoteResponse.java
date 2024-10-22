package com.idle.weather.boardvote.api.response;

import com.idle.weather.boardvote.domain.VoteType;
import io.swagger.v3.oas.annotations.media.Schema;

public record BoardVoteResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "게시글 ID", example = "1")
        Long boardId,

        @Schema(description = "투표 타입", example = "UPVOTE")
        VoteType voteType
) {
}
