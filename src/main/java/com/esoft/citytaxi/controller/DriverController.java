package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.BasicUserRequest;
import com.esoft.citytaxi.enums.DriverStatus;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.services.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/driver")
@Slf4j
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<?> saveDriver(@RequestBody final BasicUserRequest basicUserRequest){
        Driver driver = driverService.saveDriver(basicUserRequest);
        return new ResponseEntity<>(driver, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public List<Driver> searchDrivers(@RequestParam("longitude") final Double longitude,
                                      @RequestParam("latitude") final Double latitude){
        return driverService.searchDrivers(longitude, latitude);
    }

    @PutMapping("/update-location/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable final Long id,
                                            @RequestParam("longitude") final double longitude,
                                            @RequestParam("latitude") final double latitude){
        driverService.updateDriverLocation(id, longitude, latitude);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> findById(@PathVariable Long id){
        Driver driver = driverService.findById(id);
        return ResponseEntity.ok(driver);
    }

    @GetMapping
    public List<Driver> findAll(){
        return driverService.findAll();
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<Driver> updateStatus(@PathVariable final Long id,
                                               @RequestParam("status") final DriverStatus status){
        Driver driver = driverService.updateStatus(id, status);
        return ResponseEntity.ok(driver);
    }
}
