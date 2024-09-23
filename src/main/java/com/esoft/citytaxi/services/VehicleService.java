package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.VehicleRequest;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.models.Vehicle;
import com.esoft.citytaxi.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverService driverService;

    public Vehicle saveVehicle(final VehicleRequest vehicleRequest){
        Vehicle vehicle = Vehicle.builder()
                .name(vehicleRequest.getName())
                .model(vehicleRequest.getModel())
                .type(vehicleRequest.getType())
                .vehicleNumber(vehicleRequest.getVehicleNumber())
                .registrationNumber(vehicleRequest.getRegistrationNumber())
                .build();

        Driver driver = driverService.findById(vehicleRequest.getDriverId());
        Vehicle saved = vehicleRepository.save(vehicle);
        driver.setVehicle(saved);
        driverService.updateDriver(driver);

        return saved;
    }
}
