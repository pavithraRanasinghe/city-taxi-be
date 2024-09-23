package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.BasicUserRequest;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.repository.DriverRepository;
import com.esoft.citytaxi.util.LocationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public Driver saveDriver(final BasicUserRequest basicUserRequest) {
        return driverRepository.save(Driver.builder()
                .firstName(basicUserRequest.getFirstName())
                .lastName(basicUserRequest.getLastName())
                .contact(basicUserRequest.getContact())
                .build());
    }

    public List<Driver> searchDrivers(final Double longitude, final Double latitude) {
        final double distance = 0.2;
        return driverRepository.searchDrivers(longitude, latitude, distance);
    }

    public void updateDriverLocation(final Long id, final double longitude, final double latitude) {
        Driver driver = findById(id);
        driver.setLocation(LocationUtil.mapToPoint(longitude, latitude));

        driverRepository.save(driver);
    }

    public Driver findById(final Long id) {
        return driverRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Driver not found"));
    }

    public Driver updateDriver(final Driver driver){
        return driverRepository.save(driver);
    }

}
