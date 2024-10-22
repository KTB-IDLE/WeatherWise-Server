package com.idle.weather.location.api;

import com.idle.weather.location.api.port.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
}
