package com.idle.weather.board.api.response;

import com.idle.weather.board.domain.Board;

import java.util.List;

public record BoardResponseDto(
        List<BoardResponse> boards,
        boolean hasMore,
        String nextCursor
) {
    public static BoardResponseDto of(List<Board> boards, boolean hasMore) {
        return new BoardResponseDto(
                boards.stream()
                        .map(BoardResponse::from)
                        .toList(),
                hasMore,
                null
        );
    }

    public static BoardResponseDto ofForCursor(List<Board> boards, boolean hasMore, String nextCursor) {
        return new BoardResponseDto(
                boards.stream()
                        .map(BoardResponse::from)
                        .toList(),
                hasMore,
                nextCursor
        );
    }
}