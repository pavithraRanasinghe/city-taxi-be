package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.BasicUserRequest;
import com.esoft.citytaxi.dto.response.DriverResponse;
import com.esoft.citytaxi.enums.DriverStatus;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.repository.DriverRepository;
import com.esoft.citytaxi.util.LocationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DriverService {

    private final DriverRepository driverRepository;
    private final FeedbackService feedbackService;

    public DriverService(DriverRepository driverRepository, @Lazy FeedbackService feedbackService) {
        this.driverRepository = driverRepository;
        this.feedbackService = feedbackService;
    }

    public Driver saveDriver(final BasicUserRequest basicUserRequest) {
        return driverRepository.save(Driver.builder()
                .firstName(basicUserRequest.getFirstName())
                .lastName(basicUserRequest.getLastName())
                .contact(basicUserRequest.getContact())
                .status(DriverStatus.AVAILABLE)
                .build());
    }

    public List<DriverResponse> searchDrivers(final Double longitude, final Double latitude) {
        final double distance = 123;
        return driverRepository.searchDrivers(longitude, latitude, distance).stream().map(driver -> new DriverResponse(
                driver.getId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getContact(),
                driver.getLocation(),
                driver.getVehicle(),
                driver.getStatus(),
                feedbackService.getAverageRatingByDriverId(driver.getId())
        )).toList();
    }

    public void updateDriverLocation(final Long id, final double longitude, final double latitude) {
        Driver driver = findById(id);
        driver.setLocation(LocationUtil.mapToPoint(longitude, latitude));
        driverRepository.save(driver);
    }

    public Driver findById(final Long id) {
        return driverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Driver not found"));
    }

    public void updateDriver(final Driver driver){
        driverRepository.save(driver);
    }

    public List<DriverResponse> findAll(){
        return driverRepository.findAll().stream().map(driver -> new DriverResponse(
                driver.getId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getContact(),
                driver.getLocation(),
                driver.getVehicle(),
                driver.getStatus(),
                feedbackService.getAverageRatingByDriverId(driver.getId())
        )).toList();
    }

    public long count(){
        return driverRepository.count();
    }

    public Driver updateStatus(final Long id, final DriverStatus status){
        Driver driver = this.findById(id);
        driver.setStatus(status);

        return driverRepository.save(driver);
    }

    public Driver findByVehicleId(final Long vehicleId){
        return driverRepository.findDriverByVehicleId(vehicleId)
                .orElse(null);
    }

}
