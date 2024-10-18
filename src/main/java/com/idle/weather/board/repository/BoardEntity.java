package com.idle.weather.board.repository;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class BoardEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
}
