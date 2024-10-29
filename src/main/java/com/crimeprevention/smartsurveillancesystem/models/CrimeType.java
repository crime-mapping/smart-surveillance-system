package com.crimeprevention.smartsurveillancesystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
public class CrimeType extends BaseModel{
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @OneToMany(mappedBy = "crimeType")
    private List<IncidentReport> incidentReports;
    @ManyToMany(mappedBy = "crimeTypes")
    private Set<Crime> crimes= new HashSet<>();

}

