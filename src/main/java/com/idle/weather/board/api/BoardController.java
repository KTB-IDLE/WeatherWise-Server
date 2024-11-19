package com.idle.weather.board.api;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.board.api.response.BoardResponseDto;
import com.idle.weather.boardvote.api.response.BoardVoteResponse;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.common.annotation.UserId;
import com.idle.weather.test.concurrency.javalock.ReentrantLockFacade;
import com.idle.weather.test.concurrency.optimistic.OptimisticLockFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/boards")
public class BoardController {

    private final BoardService boardService;
    private final ReentrantLockFacade reentrantLockFacade;

    // 게시글 생성
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BoardResponse createBoard(@UserId Long userId, @RequestBody BoardRequest boardRequest) {
        return boardService.createBoard(userId, boardRequest);
    }

    // 게시글 단일 조회
    @GetMapping(path = "/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardResponse getBoardById(@PathVariable Long boardId) {
        return boardService.getBoardById(boardId);
    }

    // 특정 위치 반경 5km 이내 게시글 조회 (무한 스크롤 추가)
    @GetMapping(path = "/radius", produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardResponseDto getBoardsWithRadius(@RequestParam double latitude, @RequestParam double longitude,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return boardService.getBoardsWithRadius(latitude, longitude,page,size);
    }

    // 모든 게시글 조회
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardListResponse getAllBoards() {
        return boardService.getAllBoards();
    }

    // 사용자가 작성한 게시글 목록 조회
    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public BoardListResponse getUserBoards(@UserId Long userId) {
        return boardService.getUserBoards(userId);
    }

    // 게시글 수정
    @PostMapping(path = "/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BoardResponse updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest boardRequest) {
        return boardService.updateBoard(boardId, boardRequest);
    }

    // 게시글 삭제
    @DeleteMapping(path = "/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
    }

    // 투표 추가 및 변경
    @PostMapping(path = "/{boardId}/vote")
    public void addVote(@UserId Long userId, @PathVariable Long boardId, @RequestParam VoteType voteType) throws InterruptedException {
        // 우선 ReentrantLock 으로 설정
        reentrantLockFacade.addVote(userId,boardId,voteType);
    }

    @GetMapping("/{boardId}/user")
    public BoardVoteResponse getUserVote(@UserId Long userId, @PathVariable Long boardId) {
        return boardService.getUserVote(userId, boardId);
    }

    // 투표 수 조회
    @GetMapping(path = "/{boardId}/upvoteCount")
    public int getUpvoteCount(@PathVariable Long boardId) {
        return boardService.getUpvoteCount(boardId);
    }

    @GetMapping(path = "/{boardId}/downvoteCount")
    public int getDownvoteCount(@PathVariable Long boardId) {
        return boardService.getDownvoteCount(boardId);
    }
}
