package com.idle.weather.test.concurrency.optimistic;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * 기존 방법
 */
@Service
@RequiredArgsConstructor
public class OptimisticLockFacadeOrigin {

    private final BoardService boardService;

    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) throws InterruptedException {
        int retryCount = 0;
        int maxRetries = 10;
        int backoffTime = 50;
        while (true) {
            try {
                boardService.addVoteForConcurrencyTestOrigin(userId,boardId,voteType);
                break;
            } catch (Exception e) {
                retryCount++;
                Thread.sleep(backoffTime);
                backoffTime *= 2;  // 백오프 시간 증가
            }
        }
        if (retryCount == maxRetries) {
            throw new RuntimeException("최대 재시도 횟수를 초과했습니다.");
        }
    }

    /**
     * ObjectOptimisticLockingFailureException 이 발생했을 때 재시도
     * 최대 5번 재시도 (maxAttempts)
     * 첫 번째 재시도 간 대기 시간을 50 mils 로 설정 (delay)
     * 재시도할 떄 마다 대기 시간이 2배로 증가 (multiplier)
     * 하지만 계속 DeadLock 이 발생하기 떄문에 while(true) 를 사용
     */
    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 50, multiplier = 2)
    )
    public void addVoteForConcurrencyTestLegacy(Long userId, Long boardId, VoteType voteType) {
        boardService.addVoteForConcurrencyTestOrigin(userId, boardId, voteType);
    }



}