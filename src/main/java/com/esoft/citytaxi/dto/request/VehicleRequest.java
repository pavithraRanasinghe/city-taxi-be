package com.esoft.citytaxi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest implements Serializable {

    private String name;
    private String model;
    private String type;
    private String vehicleNumber;
    private String registrationNumber;
    private Long driverId;
}
