package com.idle.weather.home;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @NoArgsConstructor @AllArgsConstructor
@ToString
public class WeatherResponse {
    @JsonProperty("Current_Temperature")
    private String currentTemperature;

    @JsonProperty("Minimum_Temperature")
    private String minimumTemperature;

    @JsonProperty("Maximum_Temperature")
    private String maximumTemperature;

    @JsonProperty("Sky_Condition")
    private String skyCondition;

    @JsonProperty("Precipitation_Type")
    private String precipitationType;

    @JsonProperty("Is_Rained")
    private boolean isRained;

    @JsonProperty("Is_Snowed")
    private boolean isSnowed;

    @JsonProperty("Precipitation_Amount")
    private String precipitationAmount;

    @JsonProperty("Snowfall_Amount")
    private String snowfallAmount;

    @JsonProperty("AI_message")
    private String aiMessage;
}
