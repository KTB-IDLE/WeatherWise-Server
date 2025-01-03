package com.idle.weather.board.repository;

import com.idle.weather.board.api.response.BoardResponseDto;
import com.idle.weather.board.domain.Board;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository @Slf4j
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
    private final BoardJpaRepository boardJpaRepository;

    @Override
    public List<Board> findByUser(User user) {
        return boardJpaRepository
                .findByUser(UserEntity.toEntity(user)).stream().map(BoardEntity::toDomain).collect(toList());
    }

    @Override
    public Page<Board> findByLocationWithinRadius(double latitude, double longitude , int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        return boardJpaRepository.findByLocationWithinRadiusPage(latitude , longitude,pageable).map(BoardEntity::toDomain);
    }

    @Override
    public BoardResponseDto findByLocationWithinRadiusAndCursor(double latitude, double longitude, String cursor, int size) {
        // 한 개 더 가져와서 다음 데이터 유무 확인
        int limit = size + 1;

        // 커서를 LocalDateTime으로 변환 (null이면 전체 조회)
        LocalDateTime cursorTime = (cursor != null) ? LocalDateTime.parse(cursor) : null;

        // 데이터베이스에서 결과 조회
        List<Board> boards = boardJpaRepository
                .findByLocationWithinRadiusAndCursor(latitude, longitude, cursorTime, limit)
                .stream()
                .map(BoardEntity::toDomain)
                .collect(Collectors.toList());

        // 다음 데이터가 있는지 확인
        boolean hasMore = boards.size() > size;
        if (hasMore) {
            // 다음 페이지 확인용 데이터 제거
            boards.remove(boards.size() - 1);
        }

        // 다음 커서를 계산 (마지막 게시글의 createdAt)
        String nextCursor = boards.isEmpty() ? null : boards.get(boards.size() - 1).getCreatedAt().toString();

        return BoardResponseDto.ofForCursor(boards, hasMore, nextCursor);
    }

    @Override
    public Optional<Board> findByIdWithPessimisticLock(Long boardId) {
        return boardJpaRepository.findByIdWithPessimisticLock(boardId).map(BoardEntity::toDomain);
    }

    @Override
    public Optional<BoardEntity> findByIdWithOptimisticLock(Long boardId) {
        return boardJpaRepository.findByIdWithOptimisticLock(boardId);
    }

    @Override
    public void saveForOptimisticLock(BoardEntity board) {
        boardJpaRepository.save(board);
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
    public Optional<BoardEntity> findByIdForLegacy(Long id) {
        return boardJpaRepository.findById(id);
    }

    @Override
    public List<Board> findAll() {
        return boardJpaRepository.findAll().stream().map(BoardEntity::toDomain).collect(toList());
    }

    @Override
    public void delete(Board board) {
        boardJpaRepository.delete(BoardEntity.toEntity(board));
    }

    @Override
    public List<BoardEntity> findByLocationWithinRadiusForLegacy(double latitude, double longitude) {
        return null;
    }

    @Override
    public List<BoardEntity> findByUserForLegacy(UserEntity user) {
        return boardJpaRepository.findByUser(user);
    }

    @Override
    public void saveForLegacy(BoardEntity board) {
        boardJpaRepository.save(board);
    }

}
