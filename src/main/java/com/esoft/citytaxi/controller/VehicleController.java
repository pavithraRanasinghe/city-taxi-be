package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.VehicleRequest;
import com.esoft.citytaxi.models.Vehicle;
import com.esoft.citytaxi.services.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/vehicle")
@Slf4j
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<?> saveVehicle(@RequestBody VehicleRequest vehicleRequest){
        Vehicle vehicle = vehicleService.saveVehicle(vehicleRequest);
        return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
    }
}
