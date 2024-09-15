package com.esoft.citytaxi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest implements Serializable {
    private String firstName;

    private String lastName;

    private String contact;

    private Double longitude;

    private Double latitude;
}