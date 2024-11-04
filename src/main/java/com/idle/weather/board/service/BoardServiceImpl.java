package com.idle.weather.board.service;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.repository.BoardJpaRepository;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.boardvote.api.response.BoardVoteResponse;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.boardvote.repository.BoardVoteJpaRepository;
import com.idle.weather.location.domain.Location;
import com.idle.weather.location.repository.LocationEntity;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j @Builder
public class BoardServiceImpl implements BoardService {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardRepository boardRepository;
    private final BoardVoteJpaRepository boardVoteRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    private static final String UPVOTE_KEY = "board:upvote";
    private static final String DOWNVOTE_KEY = "board:downvote";

    @Override
    @Transactional
    public BoardResponse createBoard(Long userId, BoardRequest boardRequest) {
        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Location location = boardRequest.locationRequest().toDomain();

        BoardEntity newBoard = BoardEntity.createNewBoard(
                user,
                LocationEntity.toEntity(location),
                boardRequest.title(),
                boardRequest.content()
        );

        return BoardResponse.from(boardJpaRepository.save(newBoard));
    }

    @Override
    public BoardResponse getBoardById(Long boardId) {
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        return BoardResponse.from(board);
    }

    @Override
    public BoardListResponse getBoardsWithRadius(double latitude, double longitude) {
        List<BoardEntity> boards = boardJpaRepository.findByLocationWithinRadius(latitude, longitude);
        System.out.println("JIWON " + boards.size());
        return BoardListResponse.from(boards);
    }

    @Override
    public BoardListResponse getAllBoards() {
        List<BoardEntity> boards = boardJpaRepository.findAll();
        return BoardListResponse.from(boards);
    }

    @Override
    public BoardListResponse getUserBoards(Long userId) {
        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<BoardEntity> userBoards = boardJpaRepository.findByUser(user);
        return BoardListResponse.from(userBoards);
    }

    @Override
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Location updatedLocation = boardRequest.locationRequest().toDomain();

        board.updateBoard(LocationEntity.toEntity(updatedLocation), boardRequest.title(), boardRequest.content());
        return BoardResponse.from(boardJpaRepository.save(board));
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardJpaRepository.delete(board);
    }

    @Override
    public int getUpvoteCount(Long boardId) {
        String upvoteKey = UPVOTE_KEY + boardId;
        Integer upvoteCount = redisTemplate.opsForValue().get(upvoteKey);

        if (upvoteCount == null) {
            BoardEntity board = boardJpaRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("Board not found"));
            upvoteCount = board.getUpvoteCount();
            redisTemplate.opsForValue().set(upvoteKey, upvoteCount);
        }

        return upvoteCount;
    }

    @Override
    public int getDownvoteCount(Long boardId) {
        String downvoteKey = DOWNVOTE_KEY + boardId;
        Integer downvoteCount = redisTemplate.opsForValue().get(downvoteKey);

        if (downvoteCount == null) {
            BoardEntity board = boardJpaRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("Board not found"));

            downvoteCount = board.getDownvoteCount();
            redisTemplate.opsForValue().set(downvoteKey, downvoteCount);
        }
        return downvoteCount;
    }

    @Override
    public BoardVoteResponse getUserVote(Long userId, Long boardId) {
        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Optional<BoardVoteEntity> currentUserVote = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user, board);
        return currentUserVote
                .map(BoardVoteResponse::from)
                .orElse(null);
    }

    @Override
    @Transactional
    public void addVote(Long userId, Long boardId, VoteType voteType) {
        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        String upvoteKey = UPVOTE_KEY + boardId;
        String downvoteKey = DOWNVOTE_KEY + boardId;

        Optional<BoardVoteEntity> currentVoteOpt = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        try {
            if (currentVoteOpt.isPresent()) {
                BoardVoteEntity currentVote = currentVoteOpt.get();

                // 동일한 투표 타입을 클릭하여 취소하는 경우
                if (currentVote.getVoteType() == voteType) {
                    if (voteType == VoteType.UPVOTE) {
                        Integer upvoteCount = redisTemplate.opsForValue().get(upvoteKey); // NullPointException 처리
                        upvoteCount = (upvoteCount == null) ? 0 : upvoteCount; // null 체크
                        if (upvoteCount > 0) {
                            redisTemplate.opsForValue().decrement(upvoteKey); // Redis에서 감소
                            board.decrementUpvote();
                            boardJpaRepository.save(board);
                        } else {
                            throw new IllegalStateException("Cannot decrease upvote count below zero.");
                        }
                    } else if (voteType == VoteType.DOWNVOTE) {
                        Integer downvoteCount = redisTemplate.opsForValue().get(downvoteKey);
                        downvoteCount = (downvoteCount == null) ? 0 : downvoteCount;
                        if (downvoteCount > 0) {
                            redisTemplate.opsForValue().decrement(downvoteKey); // Redis에서 감소
                            board.decrementDownvote();
                            boardJpaRepository.save(board);
                        } else {
                            throw new IllegalStateException("Cannot decrease downvote count below zero.");
                        }
                    }
                    boardVoteRepository.delete(currentVote); // 데이터베이스에서 삭제
                } else {
                    // 다른 투표 타입으로 변경
                    if (currentVote.getVoteType() == VoteType.UPVOTE) {
                        redisTemplate.opsForValue().decrement(upvoteKey);
                        redisTemplate.opsForValue().increment(downvoteKey);
                        board.decrementUpvote();
                        board.incrementDownvote();
                        boardJpaRepository.save(board);
                    } else if (currentVote.getVoteType() == VoteType.DOWNVOTE) {
                        redisTemplate.opsForValue().decrement(downvoteKey);
                        redisTemplate.opsForValue().increment(upvoteKey);
                        board.decrementDownvote();
                        board.incrementUpvote();
                        boardJpaRepository.save(board);
                    }
                    currentVote.updateVoteType(voteType);
                    boardVoteRepository.save(currentVote); // 데이터베이스 업데이트
                }
            } else {
                // 새로운 투표 추가
                if (voteType == VoteType.UPVOTE) {
                    redisTemplate.opsForValue().increment(upvoteKey);
                    board.incrementUpvote();
                    boardJpaRepository.save(board);
                } else if (voteType == VoteType.DOWNVOTE) {
                    redisTemplate.opsForValue().increment(downvoteKey);
                    board.incrementDownvote();
                    boardJpaRepository.save(board);
                }

                BoardVote newVote = BoardVote.builder()
                        .user(user.toDomain())
                        .board(board.toDomain())
                        .voteType(voteType)
                        .build();

                boardVoteRepository.save(BoardVoteEntity.toEntity(newVote)); // 데이터베이스에 새로운 투표 저장
            }
        } catch (Exception e) {
            // 예외 발생 시 Redis에서 카운트를 롤백 (optional)
            if (voteType == VoteType.UPVOTE) {
                redisTemplate.opsForValue().decrement(upvoteKey);
            } else if (voteType == VoteType.DOWNVOTE) {
                redisTemplate.opsForValue().decrement(downvoteKey);
            }
            throw e; // 예외 다시 발생시켜 트랜잭션 롤백
        }
    }

    @Override
    @Transactional
    // @Transactional(isolation = Isolation.DEFAULT)
    // @Transactional(isolation = Isolation.READ_COMMITTED)
    // @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    // @Transactional(isolation = Isolation.REPEATABLE_READ)
    // @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) {

        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 1. 일반 코드
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        // 2. 비관적 락 사용 코드
        /*BoardEntity board = boardJpaRepository.findByIdWithPessimisticLock(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));*/

        // 3. 낙관적 락 사용 코드
        /*BoardEntity board = boardJpaRepository.findByIdWithOptimisticLock(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));*/

        Optional<BoardVoteEntity> currentVoteOpt = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        /**
         * Race Condition 문제를 위한 테스트이기 때문에 간단하게 로직 작성
         */
        if (currentVoteOpt.isEmpty()) {
            if (voteType == VoteType.UPVOTE) board.incrementUpvote();
            else board.decrementDownvote();
        }
    }

    @Override
    // @Transactional
    public void addVoteForConcurrencyTest2(Long userId, Long boardId, VoteType voteType) throws InterruptedException {
        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Optional<BoardVoteEntity> currentVoteOpt = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i <threadCount; i++) {
            executorService.submit(() -> {
                try {
                    board.incrementUpvote();
                    boardJpaRepository.saveAndFlush(board);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

    }

}
