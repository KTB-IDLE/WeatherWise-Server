package com.idle.weather.test.concurrency.kafka.producer;

import com.idle.weather.boardvote.domain.VoteType;
import com.idle.weather.test.concurrency.kafka.VoteMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
//public class KafkaProducerFacade {
//    private final StreamBridge streamBridge;
//    public void addVoteForConcurrencyTest(Long userId, Long boardId, VoteType voteType) throws InterruptedException {
//        VoteMessage message = VoteMessage.builder()
//                .voteType(voteType)
//                .boardId(boardId)
//                .userId((userId))
//                .build();
//        // Kafka Topic 에 Message 전송
//        streamBridge.send("vote" , message);
//    }
//}
