package com.idle.weather.mock;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.repository.UserJpaRepository;
import com.idle.weather.user.service.port.UserRepository;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeBoardRepository implements BoardRepository {
    private static Long id = 0L;
    private final List<Board> data = new ArrayList<>();

    @Override
    public Board save(Board board) {
        if (board.getBoardId() == null || board.getBoardId() == 0) {
            Board newBoard = Board.builder()
                    .boardId(id++)
                    .user(board.getUser())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .votes(board.getVotes())
                    .location(board.getLocation())
                    .createdAt(board.getCreatedAt())
                    .downvoteCount(board.getDownvoteCount())
                    .upvoteCount(board.getUpvoteCount())
                    .build();
            data.add(newBoard);
            return newBoard;
        } else {
            data.removeIf(item -> Objects.equals(item.getBoardId() , board.getBoardId()));
            data.add(board);
            return board;
        }
    }

    @Override
    public Board findById(Long id) {
        return data.stream().filter(item -> item.getBoardId().equals(id)).findAny()
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
    }

    @Override
    public List<Board> findByUser(User user) {
        return data.stream().filter(board -> board.getUser().getId().equals(user.getId())).collect(toList());
    }

    @Override
    public Optional<BoardEntity> findByIdForLegacy(Long id) {
        return Optional.empty();
    }



    @Override
    public Page<Board> findByLocationWithinRadius(double latitude, double longitude, int page , int size) {
        return null;
    }

    @Override
    public Optional<Board> findByIdWithPessimisticLock(Long boardId) {
        return Optional.empty();
    }

    @Override
    public Optional<BoardEntity> findByIdWithOptimisticLock(Long boardId) {
        return Optional.empty();
    }

    @Override
    public void saveForOptimisticLock(BoardEntity board) {

    }


    @Override
    public List<Board> findAll() {
        return null;
    }

    @Override
    public void delete(Board board) {

    }

    @Override
    public List<BoardEntity> findByLocationWithinRadiusForLegacy(double latitude, double longitude) {
        return null;
    }

    @Override
    public List<BoardEntity> findByUserForLegacy(UserEntity user) {
        return null;
    }

    @Override
    public void saveForLegacy(BoardEntity board) {

    }

    public int getDataSize() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }
}
