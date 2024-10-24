package com.idle.weather.board.api.response;

import com.idle.weather.board.repository.BoardEntity;

import java.util.List;

public record BoardListResponse(
        List<BoardResponse> boards
) {
    public static BoardListResponse from(List<BoardEntity> boards) {
        return new BoardListResponse(boards.stream()
                .map(BoardResponse::from)
                .toList());
    }
}
