package com.crimeprevention.smartsurveillancesystem.models;

import com.crimeprevention.smartsurveillancesystem.types.ECameraStatus;
import jakarta.persistence.Enumerated;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class SurveillanceCamera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private ECameraStatus status;

    private LocalDateTime lastMaintenanceDate;

    @OneToMany(mappedBy = "surveillanceCamera")
    private List<Footage> footageList;

}

