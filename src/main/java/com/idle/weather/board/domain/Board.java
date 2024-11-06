package com.idle.weather.board.domain;

import com.idle.weather.board.repository.BoardEntity;
import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.location.domain.Location;
import com.idle.weather.location.repository.LocationEntity;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Board {
    private Long boardId;

    private User user;

    private Location location;

    private String title;

    private String content;

    private Set<BoardVote> votes = new HashSet<>();

    private Integer upvoteCount = 0;   // Upvote count 추가

    private Integer downvoteCount = 0; // Downvote count 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // OptimisticLocking 전용 Version
    private int version;
    public void updateBoard(Location location, String title, String content) {
        this.location = location;
        this.title = title;
        this.content = content;
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
