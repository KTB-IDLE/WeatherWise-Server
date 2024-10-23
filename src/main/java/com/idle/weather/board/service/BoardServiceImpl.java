package com.idle.weather.board.service;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.board.api.request.BoardRequest;
import com.idle.weather.board.api.response.BoardListResponse;
import com.idle.weather.board.api.response.BoardResponse;
import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.repository.BoardJpaRepository;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.boardvote.repository.BoardVoteJpaRepository;
import com.idle.weather.boardvote.service.port.BoardVoteRepository;
import com.idle.weather.location.domain.Location;
import com.idle.weather.location.repository.LocationJpaRepository;
import com.idle.weather.user.domain.User;
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
    public BoardResponse createBoard(BoardRequest boardRequest) {
        User user = userJpaRepository.findById(boardRequest.userId())
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
        User user = userJpaRepository.findById(userId)
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
        return (upvoteCount != null) ? upvoteCount : 0;
    }

    @Override
    public int getDownvoteCount(Long boardId) {
        String downvoteKey = DOWNVOTE_KEY + boardId;
        Integer downvoteCount = redisTemplate.opsForValue().get(downvoteKey);
        return (downvoteCount != null) ? downvoteCount : 0;
    }

    @Override
    public void addVote(Long boardId, Long userId, VoteType voteType) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BoardEntity board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        String upvoteKey = UPVOTE_KEY + boardId;
        String downvoteKey = DOWNVOTE_KEY + boardId;

        Optional<BoardVote> currentVoteOpt = boardVoteJpaRepository.findCurrentVoteTypeByUserAndBoard(user, board);

        if (currentVoteOpt.isPresent()) {
            BoardVote currentVote = currentVoteOpt.get();

            if (currentVote.getVoteType() == voteType) {
                if (voteType == VoteType.UPVOTE) {
                    redisTemplate.opsForValue().decrement(upvoteKey);
                } else if (voteType == VoteType.DOWNVOTE) {
                    redisTemplate.opsForValue().get(downvoteKey);
                }
                boardVoteJpaRepository.removeVote(user, board);
            } else {
                if (currentVote.getVoteType() == VoteType.UPVOTE) {
                    redisTemplate.opsForValue().decrement(upvoteKey);
                    redisTemplate.opsForValue().increment(downvoteKey);
                } else if (currentVote.getVoteType() == VoteType.DOWNVOTE) {
                    redisTemplate.opsForValue().decrement(downvoteKey);
                    redisTemplate.opsForValue().increment(upvoteKey);
                }
                currentVote.updateVoteType(voteType);
                boardVoteJpaRepository.save(currentVote);
            }
        } else {
                if (voteType == VoteType.UPVOTE) {
                    redisTemplate.opsForValue().increment(upvoteKey);
                } else if (voteType == VoteType.DOWNVOTE) {
                    redisTemplate.opsForValue().increment(downvoteKey);
                }

                BoardVote newVote = BoardVote.builder()
                        .user(user)
                        .board(board)
                        .voteType(voteType)
                        .build();

                boardVoteJpaRepository.save(newVote);
            }
        }
}
