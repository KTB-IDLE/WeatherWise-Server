package com.idle.weather.board.service;

import com.idle.weather.mock.FakeBoardRepository;
import com.idle.weather.mock.FakeBoardVoteRepository;
import com.idle.weather.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardServiceTest {

    private BoardServiceImpl boardService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeBoardRepository fakeBoardRepository = new FakeBoardRepository();
        FakeBoardVoteRepository fakeBoardVoteRepository = new FakeBoardVoteRepository();
        this.boardService = BoardServiceImpl.builder()
                .boardRepository(fakeBoardRepository)
                .userRepository(fakeUserRepository)
                .boardVoteRepository(fakeBoardVoteRepository)


    }

    @Test
    public void BoardRequest_를_이용하여_게시물을_생성할_수_있다() throws Exception
    {
        //given

        //when

        //then
    }

}