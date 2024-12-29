package com.idle.weather.home;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponse(
        @JsonProperty("Current_Temperature") String currentTemperature,
        @JsonProperty("Minimum_Temperature") String minimumTemperature,
        @JsonProperty("Maximum_Temperature") String maximumTemperature,
        @JsonProperty("Sky_Condition") String skyCondition,
        @JsonProperty("Precipitation_Type") String precipitationType,
        @JsonProperty("Is_Rained") boolean isRained,
        @JsonProperty("Is_Snowed") boolean isSnowed,
        @JsonProperty("Precipitation_Amount") String precipitationAmount,
        @JsonProperty("Snowfall_Amount") String snowfallAmount,
        @JsonProperty("AI_message") String aiMessage
) {}