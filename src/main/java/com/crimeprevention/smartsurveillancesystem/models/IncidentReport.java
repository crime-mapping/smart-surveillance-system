package com.crimeprevention.smartsurveillancesystem.models;

import com.crimeprevention.smartsurveillancesystem.types.EIncidentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class IncidentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private EIncidentStatus status;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "crime_type_id")
    private CrimeType crimeType;

}



