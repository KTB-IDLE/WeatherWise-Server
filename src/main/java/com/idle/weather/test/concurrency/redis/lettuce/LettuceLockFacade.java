package com.idle.weather.test.concurrency.redis.lettuce;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LettuceLockFacade {

    private final RedisLockRepository redisLockRepository;
    private final BoardService boardService;

    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) throws InterruptedException {

        while (!redisLockRepository.lock(boardId)) {
            Thread.sleep(100);
        }

        try {
            boardService.addVoteForConcurrencyTest(userId,boardId,voteType);
        } finally {
            redisLockRepository.unlock(boardId);
        }
    }
}
