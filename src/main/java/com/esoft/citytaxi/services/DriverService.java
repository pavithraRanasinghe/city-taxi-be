package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.DriverRequest;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.repository.DriverRepository;
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

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final Double distance = 0.2;

    public Driver saveDriver(final DriverRequest driverRequest) {
        return driverRepository.save(Driver.builder()
                .firstName(driverRequest.getFirstName())
                .lastName(driverRequest.getLastName())
                .contact(driverRequest.getContact())
                .location(mapToPoint(driverRequest.getLongitude(), driverRequest.getLatitude()))
                .build());
    }

    public List<Driver> searchDrivers(final Double longitude, final Double latitude) {
        return driverRepository.searchDrivers(longitude, latitude, distance);
    }

    private Point mapToPoint(final double longitude, final double latitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        point.setSRID(4326);
        return point;
    }
}
