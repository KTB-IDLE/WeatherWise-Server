package com.idle.weather.mock;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.board.service.port.BoardRepository;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.boardvote.service.port.BoardVoteRepository;
import com.idle.weather.exception.BaseException;
import com.idle.weather.exception.ErrorCode;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * InMemory DB (H2 사용 X)
 */
public class FakeBoardVoteRepository implements BoardVoteRepository {
    private static Long id = 0L;
    private final List<BoardVote> data = new ArrayList<>();

    @Override
    public Optional<BoardVoteEntity> findCurrentVoteTypeByUserAndBoardForLegacy(UserEntity user, BoardEntity board) {
        return Optional.empty();
    }

    @Override
    public Optional<BoardVote> findCurrentVoteTypeByUserAndBoard(User user, Board board) {
        Optional<BoardVote> boardVote = data.stream().filter(bv -> bv.getBoard().getBoardId().equals(board.getBoardId()) &&
                bv.getUser().getId().equals(user.getId())).findAny();
        return boardVote;
    }

    @Override
    public void removeVote(User user, Board board) {

    }

    @Override
    public void delete(BoardVote boardVote) {
        data.remove(boardVote);
    }

    @Override
    public void save(BoardVote boardVote) {
        // TODO: 11/6/24 여기부터 채우면서 동시성 테스트 코드 작성하기
        // TODO: 11/6/24 모든 코드 다 한 번씩 테스트 코드 작성해보기
        if (boardVote.getVoteId() == null || boardVote.getVoteId() == 0) {
            BoardVote newBoardVote = BoardVote.builder()
                    .voteId(id++)
                    .build();
            data.add(newBoardVote);
        } else {
            // 같은 User 라면 기존에 것을 삭제하고 새로 추가
            data.removeIf(item -> Objects.equals(item.getVoteId() , boardVote.getVoteId()));
            data.add(boardVote);
        }

    }

    @Override
    public void saveForLegacy(BoardVoteEntity boardVote) {

    }

    @Override
    public void deleteForLegacy(BoardVoteEntity boardVote) {

    }
}
