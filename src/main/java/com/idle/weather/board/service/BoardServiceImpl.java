package com.idle.weather.board.service;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.repository.BoardJpaRepository;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.boardvote.api.response.BoardVoteResponse;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.boardvote.repository.BoardVoteJpaRepository;
import com.idle.weather.boardvote.service.port.BoardVoteRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.location.domain.Location;
import com.idle.weather.location.repository.LocationEntity;
import com.idle.weather.location.repository.LocationJpaRepository;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j @Builder @Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardVoteRepository boardVoteRepository;
    private final UserRepository userRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final LocationJpaRepository locationJpaRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    private static final String UPVOTE_KEY = "board:upvote";
    private static final String DOWNVOTE_KEY = "board:downvote";

    @Override
    @Transactional
    public BoardResponse createBoard(Long userId, BoardRequest boardRequest) {
        // User 조회
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Location 조회 또는 생성
        LocationEntity location = locationJpaRepository.findByLocationName(boardRequest.locationRequest().locationName())
                .orElseGet(() -> {
                    LocationEntity newLocation = boardRequest.locationRequest().toEntity();
                    return locationJpaRepository.save(newLocation); // 새로운 Location 저장
                });

        // Board 생성
        BoardEntity newBoard = BoardEntity.createNewBoard(
                user,
                location,  // 저장된 LocationEntity 사용
                boardRequest.title(),
                boardRequest.content()
        );

        BoardEntity savedBoard = boardJpaRepository.save(newBoard);

        // Redis 초기값 설정 (0으로 설정)
        String upvoteKey = UPVOTE_KEY + savedBoard.getBoardId();
        String downvoteKey = DOWNVOTE_KEY + savedBoard.getBoardId();
        redisTemplate.opsForValue().set(upvoteKey, 0);
        redisTemplate.opsForValue().set(downvoteKey, 0);

        return BoardResponse.from(savedBoard.toDomain());
    }

    @Override
    public BoardResponse getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId);
        return BoardResponse.from(board);
    }

    @Override
    public BoardListResponse getBoardsWithRadius(double latitude, double longitude) {
        List<Board> boards = boardRepository.findByLocationWithinRadius(latitude, longitude);
        return BoardListResponse.from(boards);
    }

    @Override
    public BoardListResponse getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return BoardListResponse.from(boards);
    }

    @Override
    public BoardListResponse getUserBoards(Long userId) {
        User user = userRepository.findById(userId);
        List<Board> userBoards = boardRepository.findByUser(user);
        return BoardListResponse.from(userBoards);
    }

    @Override
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {

        BoardEntity board = boardRepository
                .findByIdForLegacy(boardId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));

        LocationEntity updatedLocation = boardRequest.locationRequest().toEntity();

        board.updateBoard(updatedLocation, boardRequest.title(), boardRequest.content());
        boardJpaRepository.save(board);
        return BoardResponse.from(boardRepository.save(board.toDomain()));
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId);
        boardRepository.delete(board);
    }

    @Override
    public int getUpvoteCount(Long boardId) {
        String upvoteKey = UPVOTE_KEY + boardId;
        Integer upvoteCount = redisTemplate.opsForValue().get(upvoteKey);

        if (upvoteCount == null) {
            Board board = boardRepository.findById(boardId);
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
            Board board = boardRepository.findById(boardId);

            downvoteCount = board.getDownvoteCount();
            redisTemplate.opsForValue().set(downvoteKey, downvoteCount);
        }
        return downvoteCount;
    }

    @Override
    public BoardVoteResponse getUserVote(Long userId, Long boardId) {
        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Board board = boardRepository.findById(boardId);

        Optional<BoardVote> currentUserVote = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user.toDomain(), board);

        return currentUserVote
                .map(BoardVoteResponse::from)
                .orElse(null);
    }

    @Override
    @Transactional
    public void addVote(Long userId, Long boardId, VoteType voteType) {
        System.out.println("BoardServiceImpl.addVote");
        User user = userRepository.findById(userId);
        Board board = boardRepository.findById(boardId);

        String upvoteKey = UPVOTE_KEY + boardId;
        String downvoteKey = DOWNVOTE_KEY + boardId;

        Optional<BoardVote> currentVoteOpt = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        try {
            System.out.println("BoardServiceImpl.addVote.try");
            if (currentVoteOpt.isPresent()) {
                BoardVote currentVote = currentVoteOpt.get();
                System.out.println(1);

                // 동일한 투표 타입을 클릭하여 취소하는 경우
                if (currentVote.getVoteType() == voteType) {
                    if (voteType == VoteType.UPVOTE) {
                        Integer upvoteCount = redisTemplate.opsForValue().get(upvoteKey); // NullPointException 처리
                        upvoteCount = (upvoteCount == null) ? 0 : upvoteCount; // null 체크
                        if (upvoteCount > 0) {
                            redisTemplate.opsForValue().decrement(upvoteKey); // Redis에서 감소
                            board.decrementUpvote();
                            boardRepository.save(board);
                        } else {
                            throw new IllegalStateException("Cannot decrease upvote count below zero.");
                        }
                    } else if (voteType == VoteType.DOWNVOTE) {
                        Integer downvoteCount = redisTemplate.opsForValue().get(downvoteKey);
                        downvoteCount = (downvoteCount == null) ? 0 : downvoteCount;
                        if (downvoteCount > 0) {
                            redisTemplate.opsForValue().decrement(downvoteKey); // Redis에서 감소
                            board.decrementDownvote();
                            boardRepository.save(board);
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
                        boardRepository.save(board);
                    } else if (currentVote.getVoteType() == VoteType.DOWNVOTE) {
                        redisTemplate.opsForValue().decrement(downvoteKey);
                        redisTemplate.opsForValue().increment(upvoteKey);
                        board.decrementDownvote();
                        board.incrementUpvote();
                        boardRepository.save(board);
                    }
                    currentVote.updateVoteType(voteType);
                    boardVoteRepository.save(currentVote); // 데이터베이스 업데이트
                }
            } else {
                System.out.println(2);
                // 새로운 투표 추가
                if (voteType == VoteType.UPVOTE) {
                    System.out.println(3);
                    redisTemplate.opsForValue().increment(upvoteKey);
                    System.out.println(4);
                    board.incrementUpvote();
                    System.out.println(5);
                    boardRepository.save(board);
                } else if (voteType == VoteType.DOWNVOTE) {
                    redisTemplate.opsForValue().increment(downvoteKey);
                    board.incrementDownvote();
                    boardRepository.save(board);
                }
                System.out.println(2);

                BoardVote newVote = BoardVote.builder()
                        .user(user)
                        .board(board)
                        .voteType(voteType)
                        .build();

                boardVoteRepository.save(newVote); // 데이터베이스에 새로운 투표 저장
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
    public void addVoteForLegacy(Long userId, Long boardId, VoteType voteType) {
        UserEntity user = userRepository.findByIdForLegacy(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardRepository.findByIdForLegacy(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        String upvoteKey = UPVOTE_KEY + boardId;
        String downvoteKey = DOWNVOTE_KEY + boardId;

        Optional<BoardVoteEntity> currentVoteOpt = boardVoteRepository.findCurrentVoteTypeByUserAndBoardForLegacy(user, board);

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
                            boardRepository.saveForLegacy(board);
                        } else {
                            throw new IllegalStateException("Cannot decrease upvote count below zero.");
                        }
                    } else if (voteType == VoteType.DOWNVOTE) {
                        Integer downvoteCount = redisTemplate.opsForValue().get(downvoteKey);
                        downvoteCount = (downvoteCount == null) ? 0 : downvoteCount;
                        if (downvoteCount > 0) {
                            redisTemplate.opsForValue().decrement(downvoteKey); // Redis에서 감소
                            board.decrementDownvote();
                            boardRepository.saveForLegacy(board);
                        } else {
                            throw new IllegalStateException("Cannot decrease downvote count below zero.");
                        }
                    }
                    boardVoteRepository.deleteForLegacy(currentVote); // 데이터베이스에서 삭제
                } else {
                    // 다른 투표 타입으로 변경
                    if (currentVote.getVoteType() == VoteType.UPVOTE) {
                        redisTemplate.opsForValue().decrement(upvoteKey);
                        redisTemplate.opsForValue().increment(downvoteKey);
                        board.decrementUpvote();
                        board.incrementDownvote();
                        boardRepository.saveForLegacy(board);
                    } else if (currentVote.getVoteType() == VoteType.DOWNVOTE) {
                        redisTemplate.opsForValue().decrement(downvoteKey);
                        redisTemplate.opsForValue().increment(upvoteKey);
                        board.decrementDownvote();
                        board.incrementUpvote();
                        boardRepository.saveForLegacy(board);
                    }
                    currentVote.updateVoteType(voteType);
                    boardVoteRepository.saveForLegacy(currentVote); // 데이터베이스 업데이트
                }
            } else {
                // 새로운 투표 추가
                if (voteType == VoteType.UPVOTE) {
                    redisTemplate.opsForValue().increment(upvoteKey);
                    board.incrementUpvote();
                    boardRepository.saveForLegacy(board);
                } else if (voteType == VoteType.DOWNVOTE) {
                    redisTemplate.opsForValue().increment(downvoteKey);
                    board.incrementDownvote();
                    boardRepository.saveForLegacy(board);
                }

                BoardVoteEntity newVote = BoardVoteEntity.builder()
                        .user(user)
                        .board(board)
                        .voteType(voteType)
                        .build();

                boardVoteRepository.saveForLegacy(newVote); // 데이터베이스에 새로운 투표 저장
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
        User user = userRepository.findById(userId);

        // 1. 일반 코드
         Board board = boardRepository.findById(boardId);

        // 2. 비관적 락 사용 코드
        /*Board board = boardRepository.findByIdWithPessimisticLock(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));*/

        // 3. 낙관적 락 사용 코드
        /*BoardEntity board = boardRepository.findByIdWithOptimisticLock(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));*/
        /*Optional<BoardVoteEntity> currentVoteOpt = boardVoteRepository
                .findCurrentVoteTypeByUserAndBoardForAddVote(user, board.toDomain());*/

        Optional<BoardVote> currentVoteOpt = boardVoteRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        /**
         * Race Condition 문제를 위한 테스트이기 때문에 간단하게 로직 작성
         */
        if (currentVoteOpt.isEmpty()) {
            if (voteType == VoteType.UPVOTE) board.incrementUpvote();
            else {
                board.incrementDownvote();
            }
        }
        // 비관적 락
        // boardRepository.saveForOptimisticLock(board);
        boardRepository.save(board);
    }

}
