package com.idle.weather.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

//로그인 할때 사용
@Builder
public record AuthSignUpDto(@JsonProperty("serialId")
                            @NotNull
                            @NotBlank
                            String serialId,
                            @JsonProperty("nickname")
                            @NotNull
                            @NotBlank
                            String nickname,
                            @JsonProperty("password")
                            @NotNull
                            @NotBlank
                            String password
) {
}