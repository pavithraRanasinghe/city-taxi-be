package com.esoft.citytaxi.dto.response;

import com.esoft.citytaxi.enums.UserType;
import com.esoft.citytaxi.models.AppUser;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private UserType userType;
    private Long driverId;
    private Long passengerId;

}
