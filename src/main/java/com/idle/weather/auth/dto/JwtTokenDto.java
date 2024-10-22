package com.idle.weather.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class JwtTokenDto extends SelfValidating<JwtTokenDto> {
    @NotNull
    String accessToken;
    @NotNull String refreshToken;

    @Builder
    public JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
