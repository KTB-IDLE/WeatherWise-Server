package com.idle.weather.level.service;

import com.idle.weather.mock.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelServiceTest {

    private LevelServiceImpl levelService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.levelService = LevelServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .build();
    }

    @Test
    public void 상위_10명의_랭킹_정보와_나의_랭킹_정보를_볼_수_있다() throws Exception
    {
        //given

        //when

        //then
    }

}