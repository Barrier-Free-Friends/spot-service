package org.bf.spotservice.spot.domain;

import jakarta.persistence.*;
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

    private String address;

    private double latitude;

    private double longitude;


}
