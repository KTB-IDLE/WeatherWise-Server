package com.idle.weather.boardvote.domain;

import com.idle.weather.board.domain.Board;
import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BoardVote {
    private Long voteId;

    private User user;

    private Board board;

    private VoteType voteType;

    public void updateVoteType(VoteType newVoteType) {
        this.voteType = newVoteType;
    }
}
