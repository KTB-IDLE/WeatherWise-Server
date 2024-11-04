package com.idle.weather.board.repository;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
    private final BoardJpaRepository boardJpaRepository;

    @Override
    public List<Board> findByUser(User user) {
        return boardJpaRepository
                .findByUser(UserEntity.toEntity(user)).stream().map(BoardEntity::toDomain).collect(toList());
    }

    @Override
    public List<Board> findByLocationWithinRadius(double latitude, double longitude) {
        return boardJpaRepository
                .findByLocationWithinRadius(latitude , longitude).stream().map(BoardEntity::toDomain).collect(toList());
    }

    @Override
    public Optional<Board> findByIdWithPessimisticLock(Long boardId) {
        return boardJpaRepository.findByIdWithPessimisticLock(boardId).map(BoardEntity::toDomain);
    }

    @Override
    public Optional<Board> findByIdWithOptimisticLock(Long boardId) {
        return boardJpaRepository.findByIdWithOptimisticLock(boardId).map(BoardEntity::toDomain);
    }

    @Override
    public Board save(Board board) {
        return boardJpaRepository.save(BoardEntity.toEntity(board)).toDomain();
    }

    @Override
    public Board findById(Long id) {
        return boardJpaRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD)).toDomain();
    }

    @Override
    public List<Board> findAll() {
        return boardJpaRepository.findAll().stream().map(BoardEntity::toDomain).collect(toList());
    }

    @Override
    public void delete(Board board) {
        boardJpaRepository.delete(BoardEntity.toEntity(board));
    }
}
