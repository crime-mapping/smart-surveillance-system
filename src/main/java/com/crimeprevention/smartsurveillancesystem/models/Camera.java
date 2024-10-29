package com.crimeprevention.smartsurveillancesystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class Camera extends BaseModel{
    private String name;
    private String resolution;
    private String modelNumber;
    private String ipAddress;
    private String streamUrl;
    private boolean isConnected;
    @ManyToOne
    @JoinColumn(name = "camera_monitor")
    private User monitor;
}
