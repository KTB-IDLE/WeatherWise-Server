package com.idle.weather.board.api;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping
    public BoardResponse createBoard(@RequestBody BoardRequest boardRequest) {
        return boardService.createBoard(boardRequest);
    }

    // 게시글 단일 조회
    @GetMapping("/{boardId}")
    public BoardResponse getBoardById(@PathVariable Long boardId) {
        return boardService.getBoardById(boardId);
    }

    // 특정 위치 반경 25km 이내 게시글 조회
    @GetMapping("/radius")
    public BoardListResponse getBoardsWithRadius(@RequestParam double latitude, @RequestParam double longitude) {
        return boardService.getBoardsWithRadius(latitude, longitude);
    }

    // 모든 게시글 조회
    @GetMapping
    public BoardListResponse getAllBoards() {
        return boardService.getAllBoards();
    }

    // 사용자가 작성한 게시글 목록 조회
    @GetMapping("/user/{userId}")
    public BoardListResponse getUserBoards(@PathVariable Long userId) {
        return boardService.getUserBoards(userId);
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public BoardResponse updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest boardRequest) {
        return boardService.updateBoard(boardId, boardRequest);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
    }

    // 투표 추가 및 변경
    @PostMapping("/{boardId}/vote")
    public void addVote(@PathVariable Long boardId, @RequestParam Long userId, @RequestParam VoteType voteType) {
        boardService.addVote(boardId, userId, voteType);
    }

    // 투표 수 조회
    @GetMapping("/{boardId}/upvoteCount")
    public int getUpvoteCount(@PathVariable Long boardId) {
        return boardService.getUpvoteCount(boardId);
    }

    @GetMapping("/{boardId}/downvoteCount")
    public int getDownvoteCount(@PathVariable Long boardId) {
        return boardService.getDownvoteCount(boardId);
    }
}
