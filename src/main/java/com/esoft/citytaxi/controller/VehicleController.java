package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.VehicleRequest;
import com.esoft.citytaxi.dto.response.VehicleResponse;
import com.esoft.citytaxi.models.Vehicle;
import com.esoft.citytaxi.services.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<VehicleResponse> findAll(){
        return vehicleService.findAll();
    }
}
