package com.idle.weather.board.repository;

import com.idle.weather.board.domain.Board;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.global.BaseEntity;
import com.idle.weather.location.domain.Location;
import com.idle.weather.location.repository.LocationEntity;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

/*    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<BoardVoteEntity> votes = new HashSet<>();*/

    @Column(nullable = false)  // 명시적으로 컬럼 추가
    private Integer upvoteCount = 0;   // Upvote count 추가

    @Column(nullable = false)  // 명시적으로 컬럼 추가
    private Integer downvoteCount = 0; // Downvote count 추가

    @Version
    private int version;  // Optimistic Locking을 위한 버전 필드 추가

    public static BoardEntity createNewBoard(UserEntity user, LocationEntity location, String title, String content) {
        return BoardEntity.builder()
                .user(user)
                .location(location)
                .title(title)
                .content(content)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
    }

    public BoardEntity updateBoard(LocationEntity location, String title, String content) {
        this.location = location;
        this.title = title;
        this.content = content;
        return this;
    }

    // 투표 수 증가 및 감소 메서드
    public void incrementUpvote() {
        this.upvoteCount++;
    }

    public void decrementUpvote() {
        if (this.upvoteCount > 0) {
            this.upvoteCount--;
        }
    }

    public void incrementDownvote() {
        this.downvoteCount++;
    }

    public void decrementDownvote() {
        if (this.downvoteCount > 0) {
            this.downvoteCount--;
        }
    }

    public Board toDomain() {
        return Board.builder()
                .boardId(boardId)
                .user(user.toDomain())
                // .votes(votes.stream().map(BoardVoteEntity::toDomain).collect(toSet()))
                .upvoteCount(upvoteCount)
                .downvoteCount(downvoteCount)
                .location(location.toDomain())
                .content(content)
                .title(title)
                .version(version)
                .createdAt(getCreatedAt())
                .build();
    }

    public static BoardEntity toEntity(Board board) {
        return BoardEntity.builder()
                .boardId(board.getBoardId())
                .upvoteCount(board.getUpvoteCount())
                .downvoteCount(board.getDownvoteCount())
                .user(UserEntity.toEntity(board.getUser()))
                .title(board.getTitle())
                .location(LocationEntity.toEntity(board.getLocation()))
                // .votes(board.getVotes().stream().map(BoardVoteEntity::toEntity).collect(toSet()))
                .title(board.getTitle())
                .content(board.getContent())
                .version(board.getVersion())
                .build();
    }
}
