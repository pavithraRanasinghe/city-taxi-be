package com.esoft.citytaxi.dto.response;

import com.esoft.citytaxi.models.Audit;
import com.esoft.citytaxi.models.Driver;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse implements Serializable {

    private Long id;
    private String name;
    private String model;
    private String type;
    private String vehicleNumber;
    private String registrationNumber;
    private Integer manufacturedYear;
    private String color;
    private Driver driver;
}
