package com.idle.weather.board.repository;

import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.global.BaseEntity;
import com.idle.weather.location.domain.Location;
import com.idle.weather.user.domain.User;
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
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<BoardVote> votes = new HashSet<>();

    public static BoardEntity createNewBoard(User user, Location location, String title, String content) {
        return BoardEntity.builder()
                .user(user)
                .location(location)
                .title(title)
                .content(content)
                .build();
    }

    public BoardEntity updateBoard(Location location, String title, String content) {
        this.location = location;
        this.title = title;
        this.content = content;
        return this;
    }
}
