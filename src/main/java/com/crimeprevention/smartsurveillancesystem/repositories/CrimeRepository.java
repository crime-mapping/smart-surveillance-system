package com.crimeprevention.smartsurveillancesystem.repositories;


import com.crimeprevention.smartsurveillancesystem.models.CrimeType;
import com.crimeprevention.smartsurveillancesystem.types.ECaseStatus;
import com.crimeprevention.smartsurveillancesystem.models.Crime;
import com.crimeprevention.smartsurveillancesystem.types.ECrimeType;
import com.crimeprevention.smartsurveillancesystem.types.EEmergencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CrimeRepository extends JpaRepository<Crime, Long> {
    List<Crime> findByEEmergencyLevel(EEmergencyLevel emergencyLevel);

    List<Crime> findByECaseStatus(ECaseStatus caseStatus);

    List<Crime> findByCrimeTypesContaining(CrimeType crimeType);

    List<Crime> findByTimeOfOccurenceBetween(Date startDate, Date endDate);

    List<Crime> findByCrimeLocationContaining(String location);
}