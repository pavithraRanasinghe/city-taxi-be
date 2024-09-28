package com.esoft.citytaxi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DashboardResponse implements Serializable {

    private long driverCount;
    private long passengerCount;
    private long onGoingTripCount;
    private double totalEarning;
}
