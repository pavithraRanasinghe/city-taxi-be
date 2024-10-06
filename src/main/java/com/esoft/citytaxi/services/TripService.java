package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.BasicUserRequest;
import com.esoft.citytaxi.dto.request.OperatorTripRequest;
import com.esoft.citytaxi.dto.request.TripRequest;
import com.esoft.citytaxi.dto.response.DriverActivityResponse;
import com.esoft.citytaxi.enums.DriverStatus;
import com.esoft.citytaxi.enums.TripStatus;
import com.esoft.citytaxi.exceptions.NotFoundException;
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

    public Trip startTrip(final TripRequest tripRequest) {
        Driver driver = driverService.findById(tripRequest.getDriverId());
        Passenger passenger = passengerService.findById(tripRequest.getPassengerId());
        Trip trip = Trip.builder()
                .date(LocalDate.now())
                .bookedTime(LocalTime.now())
                .driver(driver)
                .passenger(passenger)
                .startLocation(LocationUtil.mapToPoint(tripRequest.getStartLongitude(), tripRequest.getStartLatitude()))
                .startLocationName(tripRequest.getStartLocationName())
                .endLocation(LocationUtil.mapToPoint(tripRequest.getEndLongitude(), tripRequest.getEndLongitude()))
                .endLocationName(tripRequest.getEndLocationName())
                .price(tripRequest.getPrice())
                .status(TripStatus.PENDING)
                .build();

        Trip startedTrip = tripRepository.save(trip);

        driver.setStatus(DriverStatus.AVAILABLE);
        driverService.updateDriver(driver);

        return startedTrip;
    }

    public void updateTripStatus(final Long id, final TripStatus status) {
        Trip trip = findById(id);
        Driver driver = trip.getDriver();
        Passenger passenger = trip.getPassenger();
        if (status.equals(TripStatus.CONFIRM)) {
            driver.setStatus(DriverStatus.BUSY);
            trip.setStartTime(LocalTime.now());

            passenger.setOnTrip(true);
        }

        trip.setStatus(status);
        driverService.updateDriver(driver);
        passengerService.updatePassenger(passenger);
        tripRepository.save(trip);
    }

    public Trip findById(final Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
    }

    public Trip endTrip(final Long id) {
        Trip trip = findById(id);
        Driver driver = trip.getDriver();
        Passenger passenger = trip.getPassenger();

        trip.setStatus(TripStatus.COMPLETE);
        trip.setEndTime(LocalTime.now());

        driver.setStatus(DriverStatus.AVAILABLE);
        driverService.updateDriver(driver);

        passenger.setOnTrip(false);
        passengerService.updatePassenger(passenger);

        return tripRepository.save(trip);
    }

    public List<Trip> filterAllTripByDriverAndPassenger(Long driverId, Long passengerId) {
        return tripRepository.findByDriverIdOrPassengerId(driverId, passengerId);
    }

    public long countOngoingTrips() {
        List<TripStatus> ongoingStatuses = List.of(TripStatus.CONFIRM);
        return tripRepository.countOngoingTrips(ongoingStatuses);
    }

    public long getTripCountByDriverIdAndStatusList(Long driverId, List<TripStatus> statusList) {
        return tripRepository.findOngoingTripsByDriverId(driverId, statusList);
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

    public List<Trip> getTripsByDriverIdAndStatus(Long driverId, TripStatus status) {
        return tripRepository.findTripsByDriverIdAndStatus(driverId, status);
    }

    public Trip getConfirmedTripsByPassengerId(Long passengerId) {
        return tripRepository.findTripsByPassengerIdAndStatus(passengerId, TripStatus.CONFIRM)
                .orElseThrow(() -> new NotFoundException("Started trip not found"));
    }

    public List<Trip> findAllTripByPassengerId(Long passengerId) {
        return tripRepository.findTripsByPassengerId(passengerId);
    }

    public List<Trip> getTripsByStatusAndDriverId(TripStatus status, Long driverId) {
        return tripRepository.findTripsByStatusAndDriverId(status, driverId);
    }

    public List<Trip> getTripsByStatusAndPassengerId(TripStatus status, Long passengerId) {
        return tripRepository.findTripsByStatusAndPassengerId(status, passengerId);
    }

    public Trip bookTripByOperator(final OperatorTripRequest tripRequest) {
        Driver driver = driverService.findById(tripRequest.getDriverId());
        Passenger passenger = passengerService.savePassenger(
                BasicUserRequest.builder()
                        .firstName(tripRequest.getPassengerFirstName())
                        .lastName(tripRequest.getPassengerLastName())
                        .contact(tripRequest.getPassengerContact()).build()
        );
        passengerService.updatePassengerLocation(passenger.getId(), tripRequest.getStartLongitude(), tripRequest.getStartLatitude());
        Trip trip = Trip.builder()
                .date(LocalDate.now())
                .bookedTime(LocalTime.now())
                .driver(driver)
                .passenger(passenger)
                .startLocation(LocationUtil.mapToPoint(tripRequest.getStartLongitude(), tripRequest.getStartLatitude()))
                .startLocationName(tripRequest.getStartLocationName())
                .endLocation(LocationUtil.mapToPoint(tripRequest.getEndLongitude(), tripRequest.getEndLongitude()))
                .endLocationName(tripRequest.getEndLocationName())
                .price(tripRequest.getPrice())
                .status(TripStatus.PENDING)
                .build();

        Trip startedTrip = tripRepository.save(trip);

        driver.setStatus(DriverStatus.AVAILABLE);
        driverService.updateDriver(driver);

        return startedTrip;
    }
}
