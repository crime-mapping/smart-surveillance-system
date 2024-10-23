package com.crimeprevention.smartsurveillancesystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Getter @Setter
public class CrimePrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime predictionDate;
    @Column(nullable = false)
    private String crimeType;
    @Column(nullable = false)
    private String location;
    private Double probability;
    @ManyToOne
    @JoinColumn(name = "crime_type_id", nullable = false)
    private CrimeType crimeTypeEntity;

}

