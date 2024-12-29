package com.idle.weather.user.api.request;

import java.util.List;

public record SurveyRequestDto(
        List<Integer> answers // JSON 배열을 List로 매핑
) {
}