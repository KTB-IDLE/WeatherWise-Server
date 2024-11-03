package com.idle.weather.test.Isolation;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IsolationFacade {

    private final BoardService boardService;

    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) throws InterruptedException {
        while (true) {
            try {
                boardService.addVoteForConcurrencyTest(userId, boardId, voteType);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
