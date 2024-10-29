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
import com.idle.weather.boardvote.repository.BoardVoteJpaRepository;
import com.idle.weather.boardvote.service.port.BoardVoteRepository;
import com.idle.weather.location.domain.Location;
import com.idle.weather.location.repository.LocationJpaRepository;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardVoteJpaRepository boardVoteJpaRepository;
    private final UserRepository userJpaRepository;
    private final LocationJpaRepository locationJpaRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    private static final String UPVOTE_KEY = "board:upvote";
    private static final String DOWNVOTE_KEY = "board:downvote";

    @Override
    @Transactional
    public BoardResponse createBoard(Long userId, BoardRequest boardRequest) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Location location = boardRequest.locationRequest().toEntity();

        BoardEntity newBoard = BoardEntity.createNewBoard(
                user,
                location,
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
        return BoardListResponse.from(boards);
    }

    @Override
    public BoardListResponse getAllBoards() {
        List<BoardEntity> boards = boardJpaRepository.findAll();
        return BoardListResponse.from(boards);
    }

    @Override
    public BoardListResponse getUserBoards(Long userId) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<BoardEntity> userBoards = boardJpaRepository.findByUser(user);
        return BoardListResponse.from(userBoards);
    }

    @Override
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Location updatedLocation = boardRequest.locationRequest().toEntity();

        board.updateBoard(updatedLocation, boardRequest.title(), boardRequest.content());
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
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Optional<BoardVote> currentUserVote = boardVoteJpaRepository.findCurrentVoteTypeByUserAndBoard(user, board);
        return currentUserVote
                .map(BoardVoteResponse::from)
                .orElse(null);
    }

    @Override
    @Transactional
    public void addVote(Long userId, Long boardId, VoteType voteType) {
        UserEntity user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        String upvoteKey = UPVOTE_KEY + boardId;
        String downvoteKey = DOWNVOTE_KEY + boardId;

        Optional<BoardVote> currentVoteOpt = boardVoteJpaRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        try {
            if (currentVoteOpt.isPresent()) {
                BoardVote currentVote = currentVoteOpt.get();

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
                    boardVoteJpaRepository.delete(currentVote); // 데이터베이스에서 삭제
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
                    boardVoteJpaRepository.save(currentVote); // 데이터베이스 업데이트
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
                        .user(user)
                        .board(board)
                        .voteType(voteType)
                        .build();

                boardVoteJpaRepository.save(newVote); // 데이터베이스에 새로운 투표 저장
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
}
