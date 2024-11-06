package com.idle.weather.location.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter @Builder @AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Long locationId;
    private String locationName;
    private Double latitude;
    private Double longitude;

    public Location(String locationName, Double latitude, Double longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location createNewLocation(String locationName, Double latitude, Double longitude) {
        return new Location(locationName, latitude, longitude);
    }

    public Location updateLocation(String locationName, Double latitude, Double longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }
}
