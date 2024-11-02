package com.idle.weather.test.redis.redisson;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final BoardService boardService;

    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) throws InterruptedException {

        RLock lock = redissonClient.getLock(boardId.toString());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                // Lock 획득 실패
                return;
            }
            boardService.addVoteForConcurrencyTest(userId,boardId,voteType);
        } finally {
            lock.unlock();
        }
    }
}
