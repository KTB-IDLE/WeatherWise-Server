package com.idle.weather.board.repository;

import com.idle.weather.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {

    @Query("""
            SELECT b
            FROM BoardEntity b
            WHERE b.user = :user
            """)
    List<BoardEntity> findByUser(@Param("user") User user);

    // 특정 위치 반경 25km 이내 게시글 목록을 조회 (Native Query)
    @Query(value = """
            SELECT *
            FROM board b
            WHERE ST_Distance_Sphere(
                point(b.latitude, b.longitude),
                point(:latitude, :longitude)
            ) <= 25000
            """, nativeQuery = true)
    List<BoardEntity> findByLocationWithinRadius(@Param("latitude") double latitude, @Param("longitude") double longitude);
}
