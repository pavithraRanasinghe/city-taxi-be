package com.esoft.citytaxi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest implements Serializable {

    private String comment;
    private int rate;
    private Long tripId;
}
