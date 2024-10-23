package com.crimeprevention.smartsurveillancesystem.models;

import com.crimeprevention.smartsurveillancesystem.types.ECaseStatus;
import com.crimeprevention.smartsurveillancesystem.types.ECrimeType;
import com.crimeprevention.smartsurveillancesystem.types.EEmergencyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
public class Crime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String crimeName;
    private ECrimeType ECrimeType;
    private EEmergencyLevel EEmergencyLevel;
    private  String suspectDescription;
    private ECaseStatus ECaseStatus;
    private  String crimeLocation;
    private Date timeOfOccurence;

    public Crime() {
    }

    public Crime(long id, String crimeName, ECrimeType ECrimeType, EEmergencyLevel EEmergencyLevel, String suspectDescription, ECaseStatus ECaseStatus, String crimeLocation, Date timeOfOccurence) {
        this.id = id;
        this.crimeName = crimeName;
        this.ECrimeType = ECrimeType;
        this.EEmergencyLevel = EEmergencyLevel;
        this.suspectDescription = suspectDescription;
        this.ECaseStatus = ECaseStatus;
        this.crimeLocation = crimeLocation;
        this.timeOfOccurence = timeOfOccurence;
    }
    }
