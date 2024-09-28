package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.response.DashboardResponse;
import com.esoft.citytaxi.services.DriverService;
import com.esoft.citytaxi.services.PassengerService;
import com.esoft.citytaxi.services.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/dashboard")
@Slf4j
@RequiredArgsConstructor
public class DashboardController {

    private final DriverService driverService;
    private final PassengerService passengerService;
    private final TripService tripService;

    @GetMapping
    public ResponseEntity<?> dashboardDetails(){
        DashboardResponse dashboardResponse = DashboardResponse.builder()
                .driverCount(driverService.count())
                .passengerCount(passengerService.count())
                .onGoingTripCount(tripService.countOngoingTrips())
                .totalEarning(tripService.getTotalEarnings()).build();

        return ResponseEntity.ok(dashboardResponse);
    }
}
