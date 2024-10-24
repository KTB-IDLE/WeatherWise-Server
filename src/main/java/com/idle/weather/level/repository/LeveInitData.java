package com.idle.weather.level.repository;

import com.idle.weather.user.dto.type.EProvider;
import com.idle.weather.user.dto.type.ERole;
import com.idle.weather.user.repository.UserEntity;
import com.idle.weather.user.service.port.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LeveInitData {
    private final LevelInitService levelInitService;

    @PostConstruct
    public void init() {
        levelInitService.levelInit();
    }


    @Service
    @RequiredArgsConstructor
    @Transactional
    static class LevelInitService {
        private final LevelJpaRepository levelRepository;

        public void levelInit() {
            LevelEntity level1 = LevelEntity.builder()
                    .level(1)
                    .minExp(0)
                    .maxExp(100)
                    .build();
            levelRepository.save(level1);

            LevelEntity level2 = LevelEntity.builder()
                    .level(2)
                    .minExp(101)
                    .maxExp(200)
                    .build();
            levelRepository.save(level2);

            LevelEntity level3 = LevelEntity.builder()
                    .level(3)
                    .minExp(201)
                    .maxExp(300)
                    .build();
            levelRepository.save(level3);

            LevelEntity level4 = LevelEntity.builder()
                    .level(4)
                    .minExp(301)
                    .maxExp(400)
                    .build();
            levelRepository.save(level4);

            LevelEntity level5 = LevelEntity.builder()
                    .level(5)
                    .minExp(401)
                    .maxExp(500)
                    .build();
            levelRepository.save(level5);
        }
    }
}
