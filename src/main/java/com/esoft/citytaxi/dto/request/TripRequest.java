package com.esoft.citytaxi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripRequest implements Serializable {

    private double startLongitude;
    private double startLatitude;
    private double endLongitude;
    private double endLatitude;
    private Long driverId;
    private Long passengerId;
}
