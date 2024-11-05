package com.idle.weather.boardvote.service.port;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardVoteRepository {

    Optional<BoardVote> findCurrentVoteTypeByUserAndBoard(@Param("user") User user, @Param("board") Board board);
    void removeVote(@Param("user") User user, @Param("board") Board board);
    void delete(BoardVote boardVote);
    void save(BoardVote boardVote);

    /**
     * Domain 객체로 바꾸기
     */
    Optional<BoardVoteEntity> findCurrentVoteTypeByUserAndBoardForAddVote(@Param("user") User user, @Param("board") Board board);
    Optional<BoardVoteEntity> findCurrentVoteTypeByUserAndBoardForLegacy(@Param("user") UserEntity user, @Param("board") BoardEntity board);
    void saveForLegacy(BoardVoteEntity boardVote);
    void deleteForLegacy(BoardVoteEntity boardVote);
}
