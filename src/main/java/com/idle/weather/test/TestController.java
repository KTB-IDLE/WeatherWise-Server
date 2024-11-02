package com.idle.weather.test;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.test.named.NamedLockFacade;
import com.idle.weather.test.optimistic.OptimisticLockFacade;
import com.idle.weather.test.redis.lettuce.LettuceLockFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/test")
@RequiredArgsConstructor
@RestController @Slf4j
public class TestController {

    private final BoardService boardService;
    private final OptimisticLockFacade optimisticLockFacade;
    private final NamedLockFacade namedLockFacade;
    private final LettuceLockFacade lettuceLockFacade;


    // 투표 추가 및 변경 (Artillery)
    @PostMapping(path = "/{boardId}/vote/{userId}")
    public void addVote(@PathVariable Long boardId, @PathVariable Long userId ,
                        @RequestBody TestVoteRequestType voteType) throws InterruptedException {
        log.info("JIWON-CONTROLLER");

        // boardService.addVoteForConcurrencyTest(userId, boardId, voteType.getVoteType());
        // optimisticLockFacade.addVoteForConcurrencyTest(userId, boardId, voteType.getVoteType());
        lettuceLockFacade.addVoteForConcurrencyTest(userId,boardId,voteType.getVoteType());
    }

    // 투표 추가 및 변경 (ExecutorService)
    @PostMapping(path = "/{boardId}/vote/{userId}/app")
    public void addVoteInApplication(@PathVariable Long boardId, @PathVariable Long userId ,
                        @RequestBody TestVoteRequestType voteType) throws InterruptedException {
        boardService.addVoteForConcurrencyTest2(userId, boardId, voteType.getVoteType());
    }
}
