package com.idle.weather.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyDto(
        @JsonProperty("sunAnswer")
        @NotNull
        @NotBlank
        boolean runCold,
        @JsonProperty("coldAnswer")
        @NotNull
        @NotBlank
        boolean runHot,
        @JsonProperty("sweatingAnswer")
        @NotNull
        @NotBlank
        boolean runSweat
) {
}
