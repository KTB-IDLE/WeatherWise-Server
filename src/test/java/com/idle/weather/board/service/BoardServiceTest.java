package com.idle.weather.board.service;

import com.idle.weather.board.api.BoardController;
import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.board.domain.Board;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.location.api.request.LocationRequest;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.assertj.core.api.Assertions.*;

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
    public void BoardRequest_를_이용하여_게시물을_생성할_수_있다() throws Exception
    {
        //given
        User user = fakeUserRepository.findById(1L);
        LocationRequest locationRequest = new LocationRequest("test" , 99.99 , 99.99);
        BoardRequest boardRequest = new BoardRequest("test board" , "test board" , locationRequest);

        //when
        BoardResponse createBoard = boardService.createBoard(user.getId(), boardRequest);

        //then
        assertThat(createBoard.userId()).isEqualTo(user.getId());
        assertThat(createBoard.title()).isEqualTo("test board");
        assertThat(createBoard.content()).isEqualTo("test board");
    }

    @Test
    public void BoardRequest_를_이용하여_게시물을_수정할_수_있다() throws Exception
    {
        //given
        User user = fakeUserRepository.findById(1L);
        Board board = Board.builder()
                .upvoteCount(0)
                .downvoteCount(0)
                .title("test title")
                .content("test content")
                .createdAt(LocalDateTime.now())
                .votes(new HashSet<>())
                .location(fakeLocationRepository.getTestLocation())
                .user(user)
                .build();
        Board savedBoard = fakeBoardRepository.save(board);

        // 수정할 BoardRequest
        LocationRequest locationRequest = new LocationRequest("수정된 location" , 99.99,99.99);
        BoardRequest boardRequest = new BoardRequest("수정된 test title" , "수정된 test content",locationRequest);

        //when
        BoardResponse boardResponse = boardService.updateBoard(savedBoard.getBoardId(), boardRequest);

        //then
        assertThat(boardResponse.content()).isEqualTo("수정된 test content");
        assertThat(boardResponse.title()).isEqualTo("수정된 test title");
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
        assertThat(userBoards.boards().size()).isEqualTo(2);
    }

    @Test
    public void 기존_코드는_멀티_스레드_환경에서_안전하지_않다() throws Exception
    {
        //given
        // 100명의 유저를 생성하는 로직
        createUsersData();

        // 1L 을 가지는 User 가 게시글을 작성했다고 가정
        User user = fakeUserRepository.findById(1L);
        Board board = Board.builder()
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
        Board createBoard = fakeBoardRepository.save(board);

        // 동시에 100개의 요청
        int threadCount = 10;
        // 32개의 쓰레드를 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 1; i <= threadCount; i++) {
            int userId = i;  // i 값을 복사하여 각 스레드가 고유하게 사용할 수 있도록 함

            executorService.submit(() -> {
                try {
                    // 기존 코드
                    boardService.addVoteForConcurrencyTest((long) userId, createBoard.getBoardId(), VoteType.UPVOTE);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();


        //then
        assertThat(board.getUpvoteCount()).isNotEqualTo(100);
    }

    @Test
    public void 낙관적_락을_적용하여_멀티_스레드_환경에서도_안전하다() throws Exception
    {
        //given
        // 100명의 유저를 생성하는 로직
        createUsersData();

        // 1L 을 가지는 User 가 게시글을 작성했다고 가정
        User user = fakeUserRepository.findById(1L);
        Board board = Board.builder()
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
        Board createBoard = fakeBoardRepository.save(board);

        // 동시에 100개의 요청
        int threadCount = 10;
        // 32개의 쓰레드를 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // ReentrantLock 사용
        Lock lock = new ReentrantLock();

        //when
        for (int i = 1; i <= threadCount; i++) {
            int userId = i;
            executorService.submit(() -> {
                lock.lock();
                try {
                    System.out.println("boardService.addVote 전");
                    boardService.addVote((long) userId,createBoard.getBoardId(),VoteType.UPVOTE);
                    System.out.println("boardService.addVote 후");
                } finally {
                    lock.unlock();
                }
            });
        }
        latch.await();


        //then
        assertThat(board.getUpvoteCount()).isEqualTo(100);
    }
    @Test
    public void VoteType_를_이용하여_게시글에_싫어요를_투표할_수_있고_멀티_스레드_환경에서도_안전하다() throws Exception
    {
        //given

        //when

        //then
    }

    private void createUsersData() {
        for (int i = 0; i <= 100; i++) {
            fakeUserRepository.save(User.builder()
                    .missionHistories(new ArrayList<>())
                    .nickname("CIAN" + i)
                    .password("!234" + i)
                    .isCompletedSurvey(true)
                    .easilyCold(true)
                    .easilyHot(false)
                    .easilySweat(true)
                    .level(1)
                    .point(0)
                    .build());

        }
    }


}