package com.idle.weather.location.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
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
