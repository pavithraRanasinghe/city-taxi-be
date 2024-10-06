package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.TripRequest;
import com.esoft.citytaxi.dto.response.BaseResponse;
import com.esoft.citytaxi.dto.response.DriverActivityResponse;
import com.esoft.citytaxi.enums.TripStatus;
import com.esoft.citytaxi.models.Trip;
import com.esoft.citytaxi.services.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/trip")
@Slf4j
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<?> startTrip(@RequestBody final TripRequest tripRequest){
        Trip trip = tripService.startTrip(tripRequest);
        return new ResponseEntity<>(trip, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable final Long id){
        Trip trip = tripService.findById(id);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable final Long id,
                                          @RequestParam("status") final TripStatus status){
        tripService.updateTripStatus(id, status);
        return ResponseEntity.ok(new BaseResponse("success"));
    }

    @PutMapping("/{id}/end-trip")
    public ResponseEntity<?> endTrip(@PathVariable final Long id){
        Trip trip = tripService.endTrip(id);
        return new ResponseEntity<>(trip, HttpStatus.OK);
    }
    @GetMapping("/filter")
    public List<Trip> getTrips(@RequestParam(required = false) Long driverId,
                               @RequestParam(required = false) Long passengerId) {
        return tripService.filterAllTripByDriverAndPassenger(driverId, passengerId);
    }

    @GetMapping("/recent-activity")
    public List<DriverActivityResponse> findRecentDriverActivity(){
        return tripService.getRecentTripsByDriver();
    }

    @GetMapping("/driver")
    public List<Trip> getTripsByDriverIdAndStatus(@RequestParam("driverId") Long driverId, @RequestParam("status") TripStatus status) {
        return tripService.getTripsByDriverIdAndStatus(driverId, status);
    }

    @GetMapping("/passenger")
    public ResponseEntity<Trip> getConfirmedTripsByPassengerId(@RequestParam Long passengerId) {
        Trip trip = tripService.getConfirmedTripsByPassengerId(passengerId);
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/passenger/{id}")
    public List<Trip> findAllTripByPassenger(@PathVariable Long id) {
        return tripService.findAllTripByPassengerId(id);
    }
}
