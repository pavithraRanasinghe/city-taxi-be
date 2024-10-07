package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.VehicleRequest;
import com.esoft.citytaxi.dto.response.VehicleResponse;
import com.esoft.citytaxi.exceptions.NotFoundException;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.models.Vehicle;
import com.esoft.citytaxi.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverService driverService;

    public Vehicle saveVehicle(final VehicleRequest vehicleRequest){
        Driver driver = driverService.findById(vehicleRequest.getDriverId());
        Vehicle vehicle;
        if(Objects.nonNull(vehicleRequest.getId())){
            vehicle = vehicleRepository.findById(vehicleRequest.getId())
                    .orElseThrow(()-> new NotFoundException("Vehicle not found"));
            vehicle.setName(vehicleRequest.getName());
            vehicle.setModel(vehicleRequest.getModel());
            vehicle.setType(vehicleRequest.getType());
            vehicle.setVehicleNumber(vehicleRequest.getVehicleNumber());
            vehicle.setRegistrationNumber(vehicleRequest.getRegistrationNumber());
            vehicle.setManufacturedYear(vehicleRequest.getManufacturedYear());
            vehicle.setColor(vehicleRequest.getColor());
        }else {
            vehicle = Vehicle.builder()
                    .name(vehicleRequest.getName())
                    .model(vehicleRequest.getModel())
                    .type(vehicleRequest.getType())
                    .vehicleNumber(vehicleRequest.getVehicleNumber())
                    .registrationNumber(vehicleRequest.getRegistrationNumber())
                    .manufacturedYear(vehicleRequest.getManufacturedYear())
                    .color(vehicleRequest.getColor())
                    .build();
        }

        Vehicle saved = vehicleRepository.save(vehicle);
        driver.setVehicle(saved);
        driverService.updateDriver(driver);

        return saved;
    }

    public List<VehicleResponse> findAll(){
        return vehicleRepository.findAll().stream().map(vehicle -> {

            Driver driver = driverService.findByVehicleId(vehicle.getId());

            return VehicleResponse.builder()
                    .id(vehicle.getId())
                    .name(vehicle.getName())
                    .type(vehicle.getType())
                    .model(vehicle.getModel())
                    .color(vehicle.getColor())
                    .manufacturedYear(vehicle.getManufacturedYear())
                    .registrationNumber(vehicle.getRegistrationNumber())
                    .vehicleNumber(vehicle.getVehicleNumber())
                    .driver(driver).build();
                }
        ).toList();
    }
}
