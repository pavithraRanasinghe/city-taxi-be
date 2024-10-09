package com.esoft.citytaxi.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "vehicle")
public class Vehicle extends Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "type")
    private String type;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "manufactured_year")
    private Integer manufacturedYear;

    @Column(name = "color")
    private String color;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "driver_id")
    private Driver driver;

}
