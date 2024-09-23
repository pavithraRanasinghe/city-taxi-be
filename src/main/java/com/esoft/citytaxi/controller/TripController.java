package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.TripRequest;
import com.esoft.citytaxi.enums.TripStatus;
import com.esoft.citytaxi.models.Trip;
import com.esoft.citytaxi.services.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable final Long id,
                                          @RequestParam("status") final TripStatus status){
        tripService.updateTripStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/end-trip")
    public ResponseEntity<?> endTrip(@PathVariable final Long id,
                                     @RequestParam("longitude") final double longitude,
                                     @RequestParam("latitude") final double latitude,
                                     @RequestParam("price") final double price){
        Trip trip = tripService.endTrip(id, longitude, latitude, price);
        return new ResponseEntity<>(trip, HttpStatus.OK);
    }
}
