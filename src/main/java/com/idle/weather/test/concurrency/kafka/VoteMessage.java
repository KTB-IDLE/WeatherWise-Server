package com.idle.weather.test.concurrency.kafka;

import com.idle.weather.boardvote.domain.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class VoteMessage {
    private VoteType voteType;
    private Long userId;
    private Long boardId;
}
