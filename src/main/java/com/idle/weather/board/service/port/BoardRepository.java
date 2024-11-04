package com.idle.weather.board.service.port;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    // TODO: 11/4/24 여기서부터 시작 BoardJpaRepository 에 있는거 옮기면서 도메인으로 바꿔서 가져오기
    List<Board> findByUser(@Param("user") User user);

    List<Board> findByLocationWithinRadius(@Param("latitude") double latitude, @Param("longitude") double longitude);

    Optional<Board> findByIdWithPessimisticLock(Long boardId);
    Optional<Board> findByIdWithOptimisticLock(Long boardId);
    Board save(Board board);
    Board findById(Long id);
    List<Board> findAll();
    void delete(Board board);
}
