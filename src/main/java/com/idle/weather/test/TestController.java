package com.idle.weather.test;

import com.idle.weather.board.api.port.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/test")
@RequiredArgsConstructor
@RestController @Slf4j
public class TestController {

    private final BoardService boardService;
    // 투표 추가 및 변경
    @PostMapping(path = "/{boardId}/vote/{userId}")
    public void addVote(@PathVariable Long boardId, @PathVariable Long userId ,
                        @RequestBody TestVoteRequestType voteType) {
        log.info("1");
        boardService.addVoteForConcurrencyTest(userId, boardId, voteType.getVoteType());
    }
}
