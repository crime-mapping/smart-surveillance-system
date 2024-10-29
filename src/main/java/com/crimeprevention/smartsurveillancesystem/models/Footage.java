package com.crimeprevention.smartsurveillancesystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Getter @Setter
public class Footage extends BaseModel{
    @Column(nullable = false)
    private String filePath;
    private LocalDateTime timestamp;
    private Boolean processed;
    @ManyToOne
    @JoinColumn(name = "camera_id", nullable = false)
    private Camera camera;
    @OneToOne
    @JoinColumn(name = "incident_report_id")
    private IncidentReport incidentReport;
}
