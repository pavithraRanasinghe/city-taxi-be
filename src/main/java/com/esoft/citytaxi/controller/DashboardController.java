package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.response.AdminDashboardResponse;
import com.esoft.citytaxi.dto.response.DriverDashboardResponse;
import com.esoft.citytaxi.enums.TripStatus;
import com.esoft.citytaxi.services.DriverService;
import com.esoft.citytaxi.services.FeedbackService;
import com.esoft.citytaxi.services.PassengerService;
import com.esoft.citytaxi.services.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("v1/dashboard")
@Slf4j
@RequiredArgsConstructor
public class DashboardController {

    private final DriverService driverService;
    private final PassengerService passengerService;
    private final TripService tripService;
    private final FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<?> dashboardDetails(){
        AdminDashboardResponse adminDashboardResponse = AdminDashboardResponse.builder()
                .driverCount(driverService.count())
                .passengerCount(passengerService.count())
                .onGoingTripCount(tripService.countOngoingTrips())
                .totalEarning(tripService.getTotalEarnings()).build();

        return ResponseEntity.ok(adminDashboardResponse);
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<?> dashboardDetails(@PathVariable Long driverId){
        List<TripStatus> ongoingStatuses = Arrays.asList(TripStatus.CONFIRM, TripStatus.PENDING);
        DriverDashboardResponse adminDashboardResponse = DriverDashboardResponse.builder()
                .completedTripCount(tripService.getTripCountByDriverIdAndStatusList(driverId, List.of(TripStatus.COMPLETE)))
                .onGoingTripCount(tripService.getTripCountByDriverIdAndStatusList(driverId,ongoingStatuses))
                .rating(feedbackService.getAverageRatingByDriverId(driverId))
                .totalEarning(tripService.getTotalEarnings()).build();

        return ResponseEntity.ok(adminDashboardResponse);
    }
}
