package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.enums.TripStatus;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query(value = "SELECT * FROM driver WHERE " +
            "ST_DWithin(location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :distance) " +
            "AND status = 0",
            nativeQuery = true)
    List<Driver> searchDrivers(@Param("longitude") double longitude,
                                @Param("latitude") double latitude,
                                @Param("distance") double distance);

    @Query("SELECT d FROM driver d WHERE d.vehicle.id = :vehicleId")
    Optional<Driver> findDriverByVehicleId(@Param("vehicleId") Long vehicleId);
}
