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
    private final UserInitService userInitService;


    @PostConstruct
    public void init() {
        levelInitService.levelInit();
        userInitService.userInit();
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    static class UserInitService {
        private final UserRepository userRepository;
        public void userInit() {
            UserEntity user = UserEntity.builder()
                    .nickname("cian")
                    .serialId("serialId")
                    .role(ERole.USER)
                    .password("1234")
                    .provider(EProvider.KAKAO)
                    .build();
            userRepository.save(user);
            // 2. 권한 설정 (여기서는 단일 권한 예시)
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(user.getRole().name())
            );

            // 3. Authentication 객체 생성
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, // principal: 사용자 정보 (UserDetails)
                    null, // credentials: 보통 null을 사용
                    authorities // 권한 목록
            );

            // 4. SecurityContextHolder에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
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
