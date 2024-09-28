package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.models.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM feedback f WHERE (:driverId IS NULL OR f.trip.driver.id = :driverId) " +
            "AND (:passengerId IS NULL OR f.trip.passenger.id = :passengerId)")
    List<Feedback> findByDriverIdOrPassengerId(@Param("driverId") Long driverId, @Param("passengerId") Long passengerId);

    @Query("SELECT f FROM feedback f ORDER BY f.trip.date DESC, f.trip.startTime DESC")
    List<Feedback> findRecentFeedbacks(Pageable pageable);
}
