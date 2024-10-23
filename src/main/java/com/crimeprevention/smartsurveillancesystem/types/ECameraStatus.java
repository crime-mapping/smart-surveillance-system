package com.crimeprevention.smartsurveillancesystem.types;

import lombok.Getter;

@Getter
public enum ECameraStatus {
    ACTIVE_OPERATIONAL("Active/Operational"),
    INACTIVE_OFFLINE("Inactive/Offline"),
    FAULTY_ERROR("Faulty/Error"),
    RECORDING("Recording"),
    IDLE_STANDBY("Idle/Standby"),
    LOW_BATTERY("Low Battery"),
    DISCONNECTED_NO_SIGNAL("Disconnected/No Signal"),
    NIGHT_MODE_IR_MODE("Night Mode/IR Mode"),
    RECORDING_SUSPENDED("Recording Suspended"),
    STREAMING("Streaming");

    private final String description;

    ECameraStatus(String description) {
        this.description = description;
    }
}
