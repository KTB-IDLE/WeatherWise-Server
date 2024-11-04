package com.idle.weather.user.service;

import com.idle.weather.user.api.response.UserResponse;
import com.idle.weather.user.dto.AuthSignUpDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserServiceImpl userService;

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

        System.out.println(userResponse);

        //then
        assertThat(userResponse.userId()).isNotNull();
        assertThat(userResponse.nickname()).isEqualTo("cian.kim");
        assertThat(userResponse.serialId()).isEqualTo("cian@NAVER.COM");

    }



}