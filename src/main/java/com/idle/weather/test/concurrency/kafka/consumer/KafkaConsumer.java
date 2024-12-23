package com.idle.weather.test.concurrency.kafka.consumer;

import com.idle.weather.board.api.port.BoardService;
import com.idle.weather.test.concurrency.kafka.VoteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class KafkaConsumer {
//    private final BoardService boardService;
//    @Bean
//    public Consumer<Message<VoteMessage>> consume() {
//        return voteMessageMessage -> {
//            VoteMessage message = voteMessageMessage.getPayload();
//            boardService.addVoteForConcurrencyTest(message.getUserId(), message.getBoardId(), message.getVoteType());
//        };
//    }
//}
