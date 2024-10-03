package com.esoft.citytaxi.services;


import com.esoft.citytaxi.dto.request.BasicUserRequest;
import com.esoft.citytaxi.exceptions.NotFoundException;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.models.Passenger;
import com.esoft.citytaxi.repository.PassengerRepository;
import com.esoft.citytaxi.util.LocationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public Passenger savePassenger(final BasicUserRequest userRequest){
        Passenger passenger = Passenger.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .contact(userRequest.getContact())
                .build();

        return passengerRepository.save(passenger);
    }

    public Passenger findById(final Long id){
        return passengerRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Passenger not found"));
    }

    public List<Passenger> findAll(){
        return passengerRepository.findAll();
    }

    public long count(){
        return passengerRepository.count();
    }

    public Passenger updatePassenger(final Passenger passenger){
        return passengerRepository.save(passenger);
    }

    public void updatePassengerLocation(final Long id, final double longitude, final double latitude) {
        Passenger passenger = findById(id);
        passenger.setLocation(LocationUtil.mapToPoint(longitude, latitude));
        passengerRepository.save(passenger);
    }
}
