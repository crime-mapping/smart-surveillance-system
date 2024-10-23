package com.crimeprevention.smartsurveillancesystem.models;

import com.crimeprevention.smartsurveillancesystem.types.ECaseStatus;
import com.crimeprevention.smartsurveillancesystem.types.EEmergencyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Crime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String crimeName;
    @ManyToMany
    @JoinTable(
            name = "crime_crimeType",
            joinColumns = @JoinColumn(name = "crime_id"),
            inverseJoinColumns = @JoinColumn(name = "crimeType_id")
    )
    private Set<CrimeType> crimeTypes = new HashSet<>();
    private EEmergencyLevel EEmergencyLevel;
    private String suspectDescription;
    private ECaseStatus ECaseStatus;
    private String crimeLocation;
    private Date timeOfOccurence;

    public Crime() {
    }
    public Crime(long id, String crimeName, Set<CrimeType> crimeTypes, com.crimeprevention.smartsurveillancesystem.types.EEmergencyLevel EEmergencyLevel, String suspectDescription, com.crimeprevention.smartsurveillancesystem.types.ECaseStatus ECaseStatus, String crimeLocation, Date timeOfOccurence) {
        this.id = id;
        this.crimeName = crimeName;
        this.crimeTypes = crimeTypes;
        this.EEmergencyLevel = EEmergencyLevel;
        this.suspectDescription = suspectDescription;
        this.ECaseStatus = ECaseStatus;
        this.crimeLocation = crimeLocation;
        this.timeOfOccurence = timeOfOccurence;
    }
}
