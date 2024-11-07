package com.idle.weather.user.service;

import com.idle.weather.mock.FakeUserRepository;
import com.idle.weather.user.api.response.UserResponse;
import com.idle.weather.user.dto.AuthSignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .passwordEncoder(new BCryptPasswordEncoder())
                .build();
    }

    @Test
    public void AuthSignUp_을_이용하여_자체_회원가입을_할_수_있다() throws Exception
    {
        //given
        AuthSignUpDto signUp = AuthSignUpDto.builder()
                .nickname("cian.kim")
                .serialId("cian@NAVER.COM")
                .password("1234")
                .build();

        //when
        UserResponse userResponse = userService.signUp(signUp);

        //then
        assertThat(userResponse.userId()).isNotNull();
        assertThat(userResponse.nickname()).isEqualTo("cian.kim");
        assertThat(userResponse.serialId()).isEqualTo("cian@NAVER.COM");
    }

}