package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.TripRequest;
import com.esoft.citytaxi.dto.response.DriverActivityResponse;
import com.esoft.citytaxi.enums.DriverStatus;
import com.esoft.citytaxi.enums.TripStatus;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.models.Passenger;
import com.esoft.citytaxi.models.Trip;
import com.esoft.citytaxi.repository.TripRepository;
import com.esoft.citytaxi.util.LocationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final DriverService driverService;
    private final PassengerService passengerService;

    public Trip startTrip(final TripRequest tripRequest){
        Driver driver = driverService.findById(tripRequest.getDriverId());
        Passenger passenger = passengerService.findById(tripRequest.getPassengerId());
        Trip trip = Trip.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .driver(driver)
                .passenger(passenger)
                .startLocation(LocationUtil.mapToPoint(tripRequest.getStartLongitude(), tripRequest.getStartLatitude()))
                .startLocationName(tripRequest.getStartLocationName())
                .endLocationName(tripRequest.getEndLocationName())
                .status(TripStatus.PENDING)
                .build();

        Trip startedTrip = tripRepository.save(trip);

        driver.setStatus(DriverStatus.BUSY);
        driverService.updateDriver(driver);

        return startedTrip;
    }

    public void updateTripStatus(final Long id, final TripStatus status){
        Trip trip = findById(id);
        trip.setStatus(status);

        tripRepository.save(trip);
    }

    public Trip findById(final Long id){
        return tripRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Trip not found"));
    }

    public Trip endTrip(final Long id, final double endLongitude, final double endLatitude, final double price){
        Trip trip = findById(id);
        trip.setEndLocation(LocationUtil.mapToPoint(endLongitude,endLatitude));
        trip.setPrice(price);
        trip.setStatus(TripStatus.COMPLETE);
        trip.setEndTime(LocalTime.now());

        return tripRepository.save(trip);
    }

    public List<Trip> filterAllTripByDriverAndPassenger(Long driverId, Long passengerId) {
        return tripRepository.findByDriverIdOrPassengerId(driverId, passengerId);
    }

    public long countOngoingTrips() {
        List<TripStatus> ongoingStatuses = Arrays.asList(TripStatus.CONFIRM, TripStatus.START);
        return tripRepository.countOngoingTrips(ongoingStatuses);
    }

    public Double getTotalEarnings() {
        return tripRepository.findTotalEarnings();
    }

    public Double getTotalEarningsByDriver(Long driverId) {
        return tripRepository.findTotalEarningsByDriver(driverId);
    }

    public List<DriverActivityResponse> getRecentTripsByDriver() {
        List<Trip> tripList = tripRepository.findRecentTrips(PageRequest.of(0, 10));
        return tripList.stream().map(trip -> DriverActivityResponse.builder()
                .driver(trip.getDriver())
                .date(trip.getDate())
                .startTime(trip.getStartTime())
                .status(trip.getStatus())
                .startLocationName(trip.getStartLocationName())
                .endLocationName(trip.getEndLocationName())
                .build()).toList();
    }
}
