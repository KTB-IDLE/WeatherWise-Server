package com.idle.weather.board.repository;

import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {

    @Query("""
            SELECT b
            FROM BoardEntity b
            WHERE b.user = :user
            """)
    List<BoardEntity> findByUser(@Param("user") UserEntity user);

    // 특정 위치 반경 5km 이내 게시글 목록을 조회 (Native Query)
    @Query(value = """
        SELECT b.*
        FROM board_entity b
        JOIN location l ON b.location_id = l.location_id
        WHERE ST_Distance_Sphere(
            point(l.longitude, l.latitude),  -- 경도, 위도 순으로 수정
            point(:longitude, :latitude)     -- 경도, 위도 순으로 수정
        ) <= 5000
        """, nativeQuery = true)
    List<BoardEntity> findByLocationWithinRadius(@Param("latitude") double latitude, @Param("longitude") double longitude);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from BoardEntity b where b.boardId = :boardId")
    Optional<BoardEntity> findByIdWithPessimisticLock(Long boardId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select b from BoardEntity b where b.boardId = :boardId")
    Optional<BoardEntity> findByIdWithOptimisticLock(Long boardId);

}
