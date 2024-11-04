package com.idle.weather.board.service;

import com.idle.weather.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardServiceTest {

    private BoardServiceImpl boardService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.boardService = BoardServiceImpl.builder()
                .boardRepository()
                .boardVoteRepository()
                .redisTemplate()
                .userRepository(fakeUserRepository)
                .build();
    }

    @Test
    public void BoardRequest_를_이용하여_게시물을_생성할_수_있다() throws Exception
    {
        //given

        //when

        //then
    }

}