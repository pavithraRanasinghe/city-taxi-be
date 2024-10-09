package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
