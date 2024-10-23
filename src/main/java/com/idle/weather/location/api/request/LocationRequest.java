package com.idle.weather.location.api.request;

import com.idle.weather.location.domain.Location;
import io.swagger.v3.oas.annotations.media.Schema;

public record LocationRequest(
        @Schema(description = "위치명", example = "서울 강남구")
        String locationName,

        @Schema(description = "위도", example = "")
        Double latitude,

        @Schema(description = "경도", example = "")
        Double longitude
) {
        public Location toEntity() {
                return Location.createNewLocation(
                        locationName,
                        latitude,
                        longitude
                );
        }
}