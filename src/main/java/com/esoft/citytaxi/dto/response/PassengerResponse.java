package com.esoft.citytaxi.dto.response;

import com.esoft.citytaxi.models.AppUser;
import com.esoft.citytaxi.models.Audit;
import com.esoft.citytaxi.models.Trip;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String contact;
    private Boolean onTrip;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point location;
    private int tripCount;
}
