package com.idle.weather.mock;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.boardvote.service.port.BoardVoteRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeBoardVoteRepository implements BoardVoteRepository {
    private static Long id = 0L;
    private final List<BoardVote> data = new ArrayList<>();


    @Override
    public Optional<BoardVoteEntity> findCurrentVoteTypeByUserAndBoardForAddVote(User user, Board board) {
        return Optional.empty();
    }

    @Override
    public Optional<BoardVoteEntity> findCurrentVoteTypeByUserAndBoardForLegacy(UserEntity user, BoardEntity board) {
        return Optional.empty();
    }

    @Override
    public Optional<BoardVote> findCurrentVoteTypeByUserAndBoard(User user, Board board) {
        return Optional.empty();
    }

    @Override
    public void removeVote(User user, Board board) {

    }

    @Override
    public void delete(BoardVote boardVote) {

    }

    @Override
    public void save(BoardVote boardVote) {

    }

    @Override
    public void saveForLegacy(BoardVoteEntity boardVote) {

    }

    @Override
    public void deleteForLegacy(BoardVoteEntity boardVote) {

    }
}
