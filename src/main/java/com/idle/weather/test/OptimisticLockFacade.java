package com.idle.weather.test;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockFacade {

    private final BoardService boardService;

    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) throws InterruptedException {
        while (true) {
            try {
                boardService.addVoteForConcurrencyTest(userId,boardId,voteType);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}