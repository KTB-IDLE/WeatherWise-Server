package com.idle.weather.board.api;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.board.api.response.BoardResponseDto;
import com.idle.weather.boardvote.api.response.BoardVoteResponse;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.common.annotation.UserId;
import com.idle.weather.global.BaseResponse;
import com.idle.weather.test.concurrency.javalock.ReentrantLockFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final ReentrantLockFacade reentrantLockFacade;

    @PostMapping
    public ResponseEntity<BaseResponse<BoardResponse>> createBoard(
            @UserId Long userId, @RequestBody BoardRequest boardRequest) {
        return ResponseEntity.ok(new BaseResponse<>(boardService.createBoard(userId, boardRequest)));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BaseResponse<BoardResponse>> getBoardById(@PathVariable Long boardId) {
        return ResponseEntity.ok(new BaseResponse<>(boardService.getBoardById(boardId)));
    }

    @GetMapping("/radius")
    public ResponseEntity<BaseResponse<BoardResponseDto>> getBoardsWithRadius(
            @RequestParam double latitude, @RequestParam double longitude,
            @RequestParam(value = "cursor", required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                new BaseResponse<>(boardService.getBoardsWithRadiusAndCursor(latitude, longitude, cursor, size)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<BoardListResponse>> getAllBoards() {
        return ResponseEntity.ok(new BaseResponse<>(boardService.getAllBoards()));
    }

    @GetMapping("/user")
    public ResponseEntity<BaseResponse<BoardListResponse>> getUserBoards(@UserId Long userId) {
        return ResponseEntity.ok(new BaseResponse<>(boardService.getUserBoards(userId)));
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<BaseResponse<BoardResponse>> updateBoard(
            @PathVariable Long boardId, @RequestBody BoardRequest boardRequest) {
        return ResponseEntity.ok(new BaseResponse<>(boardService.updateBoard(boardId, boardRequest)));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boardId}/vote")
    public ResponseEntity<Void> addVote(
            @UserId Long userId, @PathVariable Long boardId, @RequestParam VoteType voteType)
            throws InterruptedException {
        reentrantLockFacade.addVote(userId, boardId, voteType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}/user")
    public ResponseEntity<BaseResponse<BoardVoteResponse>> getUserVote(
            @UserId Long userId, @PathVariable Long boardId) {
        return ResponseEntity.ok(new BaseResponse<>(boardService.getUserVote(userId, boardId)));
    }

    @GetMapping("/{boardId}/upvoteCount")
    public ResponseEntity<BaseResponse<Integer>> getUpvoteCount(@PathVariable Long boardId) {
        return ResponseEntity.ok(new BaseResponse<>(boardService.getUpvoteCount(boardId)));
    }

    @GetMapping("/{boardId}/downvoteCount")
    public ResponseEntity<BaseResponse<Integer>> getDownvoteCount(@PathVariable Long boardId) {
        return ResponseEntity.ok(new BaseResponse<>(boardService.getDownvoteCount(boardId)));
    }
}
