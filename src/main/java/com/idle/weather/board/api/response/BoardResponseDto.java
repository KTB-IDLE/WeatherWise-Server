package com.idle.weather.board.api.response;

import com.idle.weather.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder @Getter
public class BoardResponseDto {

    List<BoardResponse> boards;
    boolean hasMore;
    String nextCursor;

    public static BoardResponseDto of(List<Board> boards , boolean hasMore) {
        return BoardResponseDto.builder()
                .boards(boards.stream()
                        .map(BoardResponse::from)
                        .toList())
                .hasMore(hasMore)
                .build();
    }

    public static BoardResponseDto ofForCursor(List<Board> boards , boolean hasMore , String nextCursor) {
        return BoardResponseDto.builder()
                .boards(boards.stream()
                        .map(BoardResponse::from)
                        .toList())
                .hasMore(hasMore)
                .nextCursor(nextCursor)
                .build();
    }
}
