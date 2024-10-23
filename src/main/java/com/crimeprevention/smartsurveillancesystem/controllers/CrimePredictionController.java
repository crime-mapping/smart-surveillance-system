package com.crimeprevention.smartsurveillancesystem.controllers;


import com.crimeprevention.smartsurveillancesystem.models.CrimePrediction;
import com.crimeprevention.smartsurveillancesystem.services.CrimePredictionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crime-predictions")
@Slf4j
public class CrimePredictionController {

    private final CrimePredictionService crimePredictionService;

    @Autowired
    public CrimePredictionController(CrimePredictionService crimePredictionService) {
        this.crimePredictionService = crimePredictionService;
    }

    @PostMapping
    public ResponseEntity<CrimePrediction> createPrediction(@RequestBody CrimePrediction crimePrediction) {
        log.info("Received request to create new crime prediction");
        CrimePrediction createdPrediction = crimePredictionService.createPrediction(crimePrediction);
        return new ResponseEntity<>(createdPrediction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CrimePrediction>> getAllPredictions() {
        log.info("Received request to get all crime predictions");
        List<CrimePrediction> predictions = crimePredictionService.getAllPredictions();
        return ResponseEntity.ok(predictions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrimePrediction> getPredictionById(@PathVariable Long id) {
        log.info("Received request to get crime prediction with id: {}", id);
        CrimePrediction prediction = crimePredictionService.getPredictionById(id);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CrimePrediction>> getPredictionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Received request to get predictions between {} and {}", startDate, endDate);
        List<CrimePrediction> predictions = crimePredictionService.getPredictionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(predictions);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<CrimePrediction>> getPredictionsByLocation(@PathVariable String location) {
        log.info("Received request to get predictions for location: {}", location);
        List<CrimePrediction> predictions = crimePredictionService.getPredictionsByLocation(location);
        return ResponseEntity.ok(predictions);
    }

    @GetMapping("/high-risk")
    public ResponseEntity<List<CrimePrediction>> getHighRiskPredictions(
            @RequestParam Double threshold,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Received request to get high risk predictions with threshold: {}", threshold);
        List<CrimePrediction> predictions = crimePredictionService.getHighRiskPredictions(threshold, startDate, endDate);
        return ResponseEntity.ok(predictions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrimePrediction> updatePrediction(
            @PathVariable Long id,
            @RequestBody CrimePrediction crimePrediction) {
        log.info("Received request to update crime prediction with id: {}", id);
        CrimePrediction updatedPrediction = crimePredictionService.updatePrediction(id, crimePrediction);
        return ResponseEntity.ok(updatedPrediction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrediction(@PathVariable Long id) {
        log.info("Received request to delete crime prediction with id: {}", id);
        crimePredictionService.deletePrediction(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid request parameters: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
