package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
