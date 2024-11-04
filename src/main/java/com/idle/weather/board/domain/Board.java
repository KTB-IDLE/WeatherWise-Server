package com.idle.weather.board.domain;

import com.idle.weather.boardvote.domain.BoardVote;
import com.idle.weather.boardvote.repository.BoardVoteEntity;
import com.idle.weather.location.domain.Location;
import com.idle.weather.user.domain.User;
import com.idle.weather.user.repository.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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
}
