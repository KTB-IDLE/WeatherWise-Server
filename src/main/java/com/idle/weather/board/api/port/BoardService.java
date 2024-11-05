package com.idle.weather.board.api.port;

import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.boardvote.api.response.BoardVoteResponse;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.user.domain.User;

public interface BoardService {

    // 게시글 생성
    BoardResponse createBoard(Long userId, BoardRequest boardRequest);

    // 게시글 단일 조회
    BoardResponse getBoardById(Long boardId);

    // 특정 위치 반경 25km 이내 게시글 조회
    BoardListResponse getBoardsWithRadius(double latitude, double longitude);

    // 모든 게시글 가져오기
    BoardListResponse getAllBoards();

    // 사용자가 작성한 게시글 목록 조회
    BoardListResponse getUserBoards(Long userId);

    // 게시글 수정
    BoardResponse updateBoard(Long BoardId, BoardRequest request);

    // 게시글 삭제
    void deleteBoard(Long boardId);

    //Redis에서 투표 수 가져오기
    int getUpvoteCount(Long boardId);

    int getDownvoteCount(Long boardId);

    BoardVoteResponse getUserVote(Long userId, Long boardId);

    // 투표 추가
    void addVote(Long userId, Long boardId, VoteType voteType);
    void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType);
    void addVoteForConcurrencyTestOrigin(Long userId, Long boardId, VoteType voteType);
    void addVoteForConcurrencyTest2(Long userId, Long boardId, VoteType voteType) throws InterruptedException;
}
