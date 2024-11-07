package com.idle.weather.mock;

import com.idle.weather.level.domain.Level;
import com.idle.weather.location.domain.Location;
import com.idle.weather.location.service.port.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeLocationRepository implements LocationRepository {
    private final List<Location> data = new ArrayList<>();
    public FakeLocationRepository() {
        Location testLocation = Location.builder()
                .locationId(1L)
                .locationName("test Location")
                .latitude(99.99)
                .longitude(99.99)
                .build();
        data.add(testLocation);
    }
    public Location getTestLocation() {
        return data.get(0);
    }
}
