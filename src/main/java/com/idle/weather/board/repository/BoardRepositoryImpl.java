package com.idle.weather.board.repository;

import com.idle.weather.board.service.port.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
    private final BoardJpaRepository boardJpaRepository;
}
