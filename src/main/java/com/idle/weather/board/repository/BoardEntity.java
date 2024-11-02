package com.idle.weather.board.repository;

import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.global.BaseEntity;
import com.idle.weather.location.domain.Location;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    private Location location;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<BoardVote> votes = new HashSet<>();

    @Column(nullable = false)  // 명시적으로 컬럼 추가
    private Integer upvoteCount = 0;   // Upvote count 추가

    @Column(nullable = false)  // 명시적으로 컬럼 추가
    private Integer downvoteCount = 0; // Downvote count 추가

//    @Version
//    private int version;  // Optimistic Locking을 위한 버전 필드 추가

    public static BoardEntity createNewBoard(UserEntity user, Location location, String title, String content) {
        return BoardEntity.builder()
                .user(user)
                .location(location)
                .title(title)
                .content(content)
                .upvoteCount(0)
                .downvoteCount(0)
                .build();
    }

    public BoardEntity updateBoard(Location location, String title, String content) {
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
}
