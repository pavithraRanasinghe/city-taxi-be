package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.DriverRequest;
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
    public ResponseEntity<?> saveDriver(@RequestBody final DriverRequest driverRequest){
        Driver driver = driverService.saveDriver(driverRequest);
        return new ResponseEntity<>(driver, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public List<Driver> searchDrivers(@RequestParam("longitude") final Double longitude,
                                      @RequestParam("latitude") final Double latitude){
        return driverService.searchDrivers(longitude, latitude);
    }
}
