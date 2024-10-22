package com.idle.weather.boardvote.repository;

import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardVoteJpaRepository extends JpaRepository<BoardVote, Long> {

    @Query("""
            SELECT v
            FROM BoardVote v
            WHERE v.user = :user AND v.board = :board
            """)
    Optional<BoardVote> findCurrentVoteTypeByUserAndBoard(@Param("user") User user, @Param("board") BoardEntity board);

    @Query("""
            DELETE
            FROM BoardVote v
            WHERE v.user = :user AND v.board = :board
            """)
    void removeVote(@Param("user") User user, @Param("board") BoardEntity board);
}
