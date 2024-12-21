package com.idle.weather.user.api.request;

import java.util.List;

public class SurveyRequestDto {
    private List<Integer> answers; // JSON 배열을 List로 매핑

    // Getter와 Setter
    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }
}