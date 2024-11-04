package com.idle.weather.board.service;

import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.repository.BoardJpaRepository;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.repository.BoardVoteJpaRepository;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.service.port.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 동시성 테스트
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yaml")
class BoardServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardJpaRepository boardJpaRepository;
    @Autowired
    private BoardVoteJpaRepository boardVoteRepository;

    @Test
    void addVoteForConcurrencyTest() {
        int threadCount = 100;
        // 비동기로 실행하는 작업을 단순화 사용할 수 있도록 해주는 JAVA API
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        // 100개의 요청이 모두 끝날 때 까지 기다려야 하기 떄문에 CountDownLatch 를 사용
        // 결국 32개의 스레드를 통해 비동기 작업을 실행하고, 100개의 요청이 모두 완료될 때까지 기다린다.
        CountDownLatch latch = new CountDownLatch(threadCount);


        AtomicLong atomicLong = new AtomicLong();

        UserEntity user = userRepository.findByIdForLegacy(atomicLong.getAndIncrement())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardJpaRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Optional<BoardVote> currentVoteOpt = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        if (currentVoteOpt.isEmpty()) {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        board.incrementUpvote();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        // 예상 : 100명의 사용자가 "좋아요" 버튼을 누르면 해당 Board 의 좋아요 개수는 100개
        Assertions.assertEquals(100 , board.getUpvoteCount());
    }
}