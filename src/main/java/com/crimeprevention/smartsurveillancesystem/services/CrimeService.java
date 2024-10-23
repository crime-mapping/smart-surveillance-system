package com.crimeprevention.smartsurveillancesystem.services;


import com.crimeprevention.smartsurveillancesystem.models.Crime;
import com.crimeprevention.smartsurveillancesystem.repositories.CrimeRepository;
import com.crimeprevention.smartsurveillancesystem.types.ECaseStatus;
import com.crimeprevention.smartsurveillancesystem.types.ECrimeType;
import com.crimeprevention.smartsurveillancesystem.types.EEmergencyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CrimeService {

    @Autowired
    private CrimeRepository crimeRepository;

    public List<Crime> getAllCrimes() {
        return crimeRepository.findAll();
    }

    public Crime getCrimeById(long id) {
        return crimeRepository.findById(id).orElse(null);
    }

    public Crime createCrime(Crime crime) {
        return crimeRepository.save(crime);
    }

    public Crime updateCrime(long id, Crime crime) {
        if (crimeRepository.existsById(id)) {
            crime.setId(id);
            return crimeRepository.save(crime);
        }
        return null;
    }

    public void deleteCrime(long id) {
        crimeRepository.deleteById(id);
    }

    public List<Crime> getCrimesByType(ECrimeType crimeType) {
        return crimeRepository.findByECrimeType(crimeType);
    }

    public List<Crime> getCrimesByEmergencyLevel(EEmergencyLevel emergencyLevel) {
        return crimeRepository.findByEEmergencyLevel(emergencyLevel);
    }

    public List<Crime> getCrimesByCaseStatus(ECaseStatus caseStatus) {
        return crimeRepository.findByECaseStatus(caseStatus);
    }

    public List<Crime> getCrimesByLocation(String location) {
        return crimeRepository.findByCrimeLocationContaining(location);
    }
    public List<Crime> getCrimesByDateRange(Date startDate, Date endDate) {
        return crimeRepository.findByTimeOfOccurenceBetween(startDate, endDate);
    }
}