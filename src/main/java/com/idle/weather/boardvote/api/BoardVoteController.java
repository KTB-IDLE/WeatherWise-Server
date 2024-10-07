package com.idle.weather.boardvote.api;

import com.idle.weather.boardvote.api.port.BoardVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardVoteController {
    private final BoardVoteService boardVoteService;
}
