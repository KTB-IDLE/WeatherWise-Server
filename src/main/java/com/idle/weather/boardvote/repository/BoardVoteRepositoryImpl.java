package com.idle.weather.boardvote.repository;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.repository.BoardJpaRepository;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.service.port.BoardVoteRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardVoteRepositoryImpl implements BoardVoteRepository {

    private final BoardVoteJpaRepository boardVoteJpaRepository;

    @Override
    public Optional<BoardVoteEntity> findCurrentVoteTypeByUserAndBoardForLegacy(UserEntity user, BoardEntity board) {
        return boardVoteJpaRepository.findCurrentVoteTypeByUserAndBoard(user,board);
    }


    @Override
    public Optional<BoardVote> findCurrentVoteTypeByUserAndBoard(User user, Board board) {
        System.out.println("BoardVoteRepositoryImpl.findCurrentVoteTypeByUserAndBoard");
        return boardVoteJpaRepository.findCurrentVoteTypeByUserAndBoard(UserEntity.toEntity(user), BoardEntity.toEntity(board))
                .map(BoardVoteEntity::toDomain);
    }

    @Override
    public void removeVote(User user, Board board) {
        boardVoteJpaRepository.removeVote(UserEntity.toEntity(user) , BoardEntity.toEntity(board));
    }

    @Override
    public void delete(BoardVote boardVote) {
        boardVoteJpaRepository.delete(BoardVoteEntity.toEntity(boardVote));
    }

    @Override
    public void save(BoardVote boardVote) {
        boardVoteJpaRepository.save(BoardVoteEntity.toEntity(boardVote));
    }


    @Override
    public void saveForLegacy(BoardVoteEntity boardVote) {
        boardVoteJpaRepository.save(boardVote);
    }

    @Override
    public void deleteForLegacy(BoardVoteEntity boardVote) {
        boardVoteJpaRepository.delete(boardVote);
    }
}
