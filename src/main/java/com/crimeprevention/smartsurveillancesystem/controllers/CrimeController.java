package com.crimeprevention.smartsurveillancesystem.controllers;

import com.crimeprevention.smartsurveillancesystem.models.Crime;
import com.crimeprevention.smartsurveillancesystem.models.CrimeType;
import com.crimeprevention.smartsurveillancesystem.services.CrimeService;
import com.crimeprevention.smartsurveillancesystem.types.ECaseStatus;
import com.crimeprevention.smartsurveillancesystem.types.ECrimeType;
import com.crimeprevention.smartsurveillancesystem.types.EEmergencyLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/crimes")
@CrossOrigin(origins = "http://localhost:3000")
public class CrimeController {
    private final CrimeService crimeService;

    public CrimeController(CrimeService crimeService) {
        this.crimeService = crimeService;
    }

    // Get all crimes
    @GetMapping
    public ResponseEntity<List<Crime>> getAllCrimes() {
        List<Crime> crimes = crimeService.getAllCrimes();
        return ResponseEntity.ok(crimes);
    }

    // Get crime by ID
    @GetMapping("/{id}")
    public ResponseEntity<Crime> getCrimeById(@PathVariable long id) {
        Crime crime = crimeService.getCrimeById(id);
        if (crime != null) {
            return ResponseEntity.ok(crime);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Create a new crime
    @PostMapping
    public ResponseEntity<Crime> createCrime(@RequestBody Crime crime) {
        try {
            Crime createdCrime = crimeService.createCrime(crime);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCrime);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Update an existing crime
    @PutMapping("/{id}")
    public ResponseEntity<Crime> updateCrime(@PathVariable long id, @RequestBody Crime crime) {
        Crime updatedCrime = crimeService.updateCrime(id, crime);
        if (updatedCrime != null) {
            return ResponseEntity.ok(updatedCrime);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete a crime
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrime(@PathVariable long id) {
        try {
            crimeService.deleteCrime(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get crimes by type
    @GetMapping("/type/{crimeType}")
    public ResponseEntity<List<Crime>> getCrimesByType(@PathVariable CrimeType crimeType) {
        List<Crime> crimes = crimeService.getCrimesByType(crimeType);
        return ResponseEntity.ok(crimes);
    }

    // Get crimes by emergency level
    @GetMapping("/emergency-level/{emergencyLevel}")
    public ResponseEntity<List<Crime>> getCrimesByEmergencyLevel(@PathVariable EEmergencyLevel emergencyLevel) {
        List<Crime> crimes = crimeService.getCrimesByEmergencyLevel(emergencyLevel);
        return ResponseEntity.ok(crimes);
    }

    // Get crimes by case status
    @GetMapping("/case-status/{caseStatus}")
    public ResponseEntity<List<Crime>> getCrimesByCaseStatus(@PathVariable ECaseStatus caseStatus) {
        List<Crime> crimes = crimeService.getCrimesByCaseStatus(caseStatus);
        return ResponseEntity.ok(crimes);
    }

    // Get crimes by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Crime>> getCrimesByLocation(@PathVariable String location) {
        List<Crime> crimes = crimeService.getCrimesByLocation(location);
        return ResponseEntity.ok(crimes);
    }

    // Get crimes by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Crime>> getCrimesByDateRange(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        List<Crime> crimes = crimeService.getCrimesByDateRange(startDate, endDate);
        return ResponseEntity.ok(crimes);
    }
}
