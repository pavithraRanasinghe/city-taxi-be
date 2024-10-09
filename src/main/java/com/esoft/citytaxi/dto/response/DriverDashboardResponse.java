package com.esoft.citytaxi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DriverDashboardResponse implements Serializable {

    private long completedTripCount;
    private double rating;
    private long onGoingTripCount;
    private double totalEarning;
}
