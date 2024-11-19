package com.idle.weather.board.service.port;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    // TODO: 11/4/24 여기서부터 시작 BoardJpaRepository 에 있는거 옮기면서 도메인으로 바꿔서 가져오기
    List<Board> findByUser(@Param("user") User user);

    Page<Board> findByLocationWithinRadius(@Param("latitude") double latitude, @Param("longitude") double longitude ,
                                           int page , int size);

    Optional<Board> findByIdWithPessimisticLock(Long boardId);

    // 낙관적 락은 Entity
    Optional<BoardEntity> findByIdWithOptimisticLock(Long boardId);
    void saveForOptimisticLock(BoardEntity board);
    Board save(Board board);
    Board findById(Long id);
    Optional<BoardEntity> findByIdForLegacy(Long id);
    List<Board> findAll();
    void delete(Board board);


    /**
     * Domain 객체로 변환하기
     */
    List<BoardEntity> findByLocationWithinRadiusForLegacy(@Param("latitude") double latitude, @Param("longitude") double longitude);
    List<BoardEntity> findByUserForLegacy(@Param("user") UserEntity user);
    void saveForLegacy(BoardEntity board);
}
