package org.bf.spotservice.spot.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Entity
@Table(name="p_spot")
public class Spot {
    @Id
    @Column(name="spot_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_name", nullable = false)
    private String name;

    private String address;

    private double latitude;

    private double longitude;


    @Builder
    private Spot(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
