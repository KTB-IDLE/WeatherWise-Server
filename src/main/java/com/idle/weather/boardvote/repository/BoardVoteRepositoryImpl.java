package com.idle.weather.boardvote.repository;

import com.idle.weather.board.service.port.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardVoteRepositoryImpl implements BoardRepository {
    private final BoardVoteJpaRepository boardVoteJpaRepository;
}
