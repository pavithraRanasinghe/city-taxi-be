package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
