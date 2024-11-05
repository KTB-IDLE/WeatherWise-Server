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
    }

    @Test
    public void BoardRequest_를_이용하여_게시물을_생성할_수_있다() throws Exception
    {
        //given

        //when

        //then
    }

    @Test
    public void BoardRequest_를_이용하여_게시물을_수정할_수_있다() throws Exception
    {
        //given

        //when

        //then
    }

    @Test
    public void VoteType_를_이용하여_게시글에_좋아요를_투표할_수_있고_멀티_스레드_환경에서도_안전하다() throws Exception
    {
        //given

        //when

        //then
    }
    @Test
    public void VoteType_를_이용하여_게시글에_싫어요를_투표할_수_있고_멀티_스레드_환경에서도_안전하다() throws Exception
    {
        //given

        //when

        //then
    }


}