package com.idle.weather.test.concurrency.javalock;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ReentrantLockFacade {
    private final BoardService boardService;

    // Java 에서 제공하는 Lock 사용
    private final Lock lock = new ReentrantLock();
    public void addVote(Long userId, Long boardId, VoteType voteType) throws InterruptedException {

        // ReentrantLock 을 이용하여 Lock 걸기
        lock.lock();

        try {
            boardService.addVote(userId,boardId,voteType);
        } finally {
            // Lock 반납
            lock.unlock();
        }
    }
}
