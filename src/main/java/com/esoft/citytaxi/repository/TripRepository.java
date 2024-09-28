package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.enums.TripStatus;
import com.esoft.citytaxi.models.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t FROM trip t WHERE (:driverId IS NULL OR t.driver.id = :driverId) " +
            "AND (:passengerId IS NULL OR t.passenger.id = :passengerId)")
    List<Trip> findByDriverIdOrPassengerId(@Param("driverId") Long driverId, @Param("passengerId") Long passengerId);

    @Query("SELECT COUNT(t) FROM trip t WHERE t.status IN (:statuses)")
    long countOngoingTrips(@Param("statuses") List<TripStatus> statuses);

    @Query("SELECT SUM(t.price) FROM trip t")
    Double findTotalEarnings();

    @Query("SELECT SUM(t.price) FROM trip t WHERE t.driver.id = :driverId")
    Double findTotalEarningsByDriver(@Param("driverId") Long driverId);

    @Query("SELECT t FROM trip t ORDER BY t.date DESC, t.startTime DESC")
    List<Trip> findRecentTrips(Pageable pageable);
}
