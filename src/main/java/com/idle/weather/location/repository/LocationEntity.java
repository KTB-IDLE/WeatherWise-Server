package com.idle.weather.location.repository;

import com.idle.weather.location.domain.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder @AllArgsConstructor
@NoArgsConstructor
@Table(name = "location_entity")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    public LocationEntity(String locationName, Double latitude, Double longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationEntity createNewLocation(String locationName, Double latitude, Double longitude) {
        return new LocationEntity(locationName, latitude, longitude);
    }

    public LocationEntity updateLocation(String locationName, Double latitude, Double longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    public static LocationEntity toEntity(Location location) {
        return LocationEntity.builder()
                .locationId(location.getLocationId())
                .locationName(location.getLocationName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    public Location toDomain() {
        return Location.builder()
                .locationId(locationId)
                .locationName(locationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
