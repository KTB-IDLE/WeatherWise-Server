package com.idle.weather.board.api.response;

import com.idle.weather.board.repository.BoardEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record BoardResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long boardId,

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "게시글 제목", example = "날씨가 너무 좋아요!")
        String title,

        @Schema(description = "게시글 내용", example = "가디건 입기 너무 좋은 날씨에요~!")
        String content,

        @Schema(description = "위치 이름", example = "서울")
        String locationName,

        @Schema(description = "생성 시간")
        LocalDateTime createdAt
) {
    public static BoardResponse from(BoardEntity board) {
        return new BoardResponse(
                board.getBoardId(),
                board.getUser().getId(),
                board.getTitle(),
                board.getContent(),
                board.getLocation().getLocationName(),
                board.getCreatedAt()
        );
    }
}
