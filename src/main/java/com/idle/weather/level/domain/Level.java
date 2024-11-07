package com.idle.weather.level.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter @NoArgsConstructor @AllArgsConstructor
public class Level {
    private int level;
    private int minExp;
    private int maxExp;
}
