package com.idle.weather.boardvote.api.response;

import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.domain.VoteType;
import io.swagger.v3.oas.annotations.media.Schema;

public record BoardVoteResponse(
        @Schema(description = "투표 ID", example = "1")
        Long voteId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "게시글 ID", example = "1")
        Long boardId,

        @Schema(description = "투표 타입", example = "UPVOTE")
        VoteType voteType
) {
        public static BoardVoteResponse from(BoardVote boardVote) {
                return new BoardVoteResponse(
                        boardVote.getVoteId(),
                        boardVote.getBoard().getBoardId(),
                        boardVote.getUser().getId(),
                        boardVote.getVoteType()
                );
        }
}
