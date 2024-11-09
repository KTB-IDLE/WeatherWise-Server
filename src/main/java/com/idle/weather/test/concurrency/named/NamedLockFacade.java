package com.idle.weather.test.concurrency.named;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.boardvote.domain.VoteType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NamedLockFacade {

    private final NamedLockRepository lockRepository;
    private final BoardService boardService;

    @Transactional
    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) {
        try {
            lockRepository.getLock(boardId.toString());
            boardService.addVoteForConcurrencyTest(userId,boardId,voteType);
        } finally {
            lockRepository.releaseLock(boardId.toString());
        }
    }
}
