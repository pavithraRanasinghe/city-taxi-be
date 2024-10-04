package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.response.BaseResponse;
import com.esoft.citytaxi.models.Passenger;
import com.esoft.citytaxi.services.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/passenger")
@Slf4j
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> findById(@PathVariable Long id){
        Passenger passenger = passengerService.findById(id);
        return ResponseEntity.ok(passenger);
    }

    @GetMapping
    public List<Passenger> findAll(){
        return passengerService.findAll();
    }

    @PutMapping("/update-location/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable final Long id,
                                            @RequestParam("longitude") final double longitude,
                                            @RequestParam("latitude") final double latitude){
        passengerService.updatePassengerLocation(id, longitude, latitude);
        return ResponseEntity.ok(new BaseResponse("success"));
    }
}
