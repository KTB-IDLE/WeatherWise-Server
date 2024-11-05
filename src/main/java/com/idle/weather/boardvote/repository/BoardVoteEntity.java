package com.idle.weather.boardvote.repository;

import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BoardVoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity board;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    public void updateVoteType(VoteType newVoteType) {
        this.voteType = newVoteType;
    }

    public BoardVote toDomain() {
        return BoardVote.builder()
                .voteId(voteId)
                .user(user.toDomain())
                .board(board.toDomain())
                .voteType(voteType)
                .build();
    }

    public static BoardVoteEntity toEntity(BoardVote boardVote) {
        return BoardVoteEntity.builder()
                .voteId(boardVote.getVoteId())
                .board(BoardEntity.toEntity(boardVote.getBoard()))
                .user(UserEntity.toEntity(boardVote.getUser()))
                .voteId(boardVote.getVoteId())
                .voteType(boardVote.getVoteType())
                .build();
    }
}
