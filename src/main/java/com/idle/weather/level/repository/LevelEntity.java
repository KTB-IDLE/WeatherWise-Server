package com.idle.weather.level.repository;

import com.idle.weather.level.domain.Level;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "levels")
public class LevelEntity {
    @Id
    private int level;
    private int minExp;
    private int maxExp;

    public Level toDomain() {
        return Level.builder()
                .level(level)
                .maxExp(maxExp)
                .minExp(minExp)
                .build();
    }

    public LevelEntity toEntity(Level level) {
        return LevelEntity.builder()
                .level(level.getLevel())
                .maxExp(level.getMaxExp())
                .minExp(level.getMinExp())
                .build();
    }
}

