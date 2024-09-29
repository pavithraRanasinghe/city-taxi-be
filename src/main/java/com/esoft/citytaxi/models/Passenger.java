package com.esoft.citytaxi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "passenger")
public class Passenger extends Audit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact")
    private String contact;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "passenger", orphanRemoval = true)
    private List<Trip> tripList;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "passenger", orphanRemoval = true)
    private AppUser appUser;
}
