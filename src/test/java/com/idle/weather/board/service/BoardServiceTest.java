package com.idle.weather.board.service;

import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.domain.Board;
import com.idle.weather.location.domain.Location;
import com.idle.weather.mock.FakeBoardRepository;
import com.idle.weather.mock.FakeBoardVoteRepository;
import com.idle.weather.mock.FakeLocationRepository;
import com.idle.weather.mock.FakeUserRepository;
import com.idle.weather.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

class BoardServiceTest {

    private BoardServiceImpl boardService;
    private FakeBoardRepository fakeBoardRepository;
    private FakeUserRepository fakeUserRepository;
    private FakeLocationRepository fakeLocationRepository;
    @Mock
    private RedisTemplate<String , Integer> redisTemplate;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        fakeBoardRepository = new FakeBoardRepository();
        fakeLocationRepository = new FakeLocationRepository();
        boardService = BoardServiceImpl.builder()
                .boardRepository(fakeBoardRepository)
                .userRepository(fakeUserRepository)
                .boardVoteRepository(new FakeBoardVoteRepository())
                .redisTemplate(redisTemplate)
                .build();
        User user = User.builder()
                .id(1L)
                .missionHistories(new ArrayList<>())
                .nickname("CIAN")
                .password("!234")
                .isCompletedSurvey(true)
                .easilyCold(true)
                .easilyHot(false)
                .easilySweat(true)
                .level(1)
                .point(0)
                .build();
        fakeUserRepository.save(user);
    }

    @AfterEach
    void clear() {
        fakeUserRepository.clear();
    }

    @Test
    public void 유저가_작성한_게시글들을_확인_할_수_있다() throws Exception
    {
        //given
        User user = fakeUserRepository.findById(1L);
        Board board1 = Board.builder()
                .upvoteCount(0)
                .downvoteCount(0)
                .title("test board1")
                .content("test board1")
                .location(new Location())
                .votes(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .location(fakeLocationRepository.getTestLocation())
                .user(user)
                .build();
        fakeBoardRepository.save(board1);

        Board board2 = Board.builder()
                .upvoteCount(0)
                .downvoteCount(0)
                .title("test board2")
                .content("test board2")
                .location(new Location())
                .votes(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .location(fakeLocationRepository.getTestLocation())
                .user(user)
                .build();
        fakeBoardRepository.save(board2);

        //when
        BoardListResponse userBoards = boardService.getUserBoards(user.getId());

        //then
        Assertions.assertThat(userBoards.boards().size()).isEqualTo(2);
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