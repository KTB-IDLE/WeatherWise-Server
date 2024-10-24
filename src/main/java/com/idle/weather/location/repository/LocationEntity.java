package com.idle.weather.location.repository;

import jakarta.persistence.*;

@Entity
public class LocationEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;
}
