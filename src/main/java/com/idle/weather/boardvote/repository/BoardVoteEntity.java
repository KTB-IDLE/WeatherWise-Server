package com.idle.weather.boardvote.repository;

import jakarta.persistence.*;

@Entity
public class BoardVoteEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_vote_id")
    private Long id;
}
