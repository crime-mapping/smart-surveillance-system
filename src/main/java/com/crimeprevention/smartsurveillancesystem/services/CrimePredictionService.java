package com.crimeprevention.smartsurveillancesystem.services;

import com.crimeprevention.smartsurveillancesystem.models.CrimePrediction;
import com.crimeprevention.smartsurveillancesystem.repositories.CrimePredictionRepository;
import com.crimeprevention.smartsurveillancesystem.repositories.CrimeTypeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CrimePredictionService {
    private final CrimePredictionRepository crimePredictionRepository;
    private final CrimeTypeRepository crimeTypeRepository;

    @Autowired
    public CrimePredictionService(
            CrimePredictionRepository crimePredictionRepository,
            CrimeTypeRepository crimeTypeRepository
    ) {
        this.crimePredictionRepository = crimePredictionRepository;
        this.crimeTypeRepository = crimeTypeRepository;
    }

    public CrimePrediction createPrediction(CrimePrediction crimePrediction) {
        validatePrediction(crimePrediction);
        log.info("Creating new crime prediction for location: {}", crimePrediction.getLocation());
        return crimePredictionRepository.save(crimePrediction);
    }

    public List<CrimePrediction> getAllPredictions() {
        return crimePredictionRepository.findAll();
    }

    public CrimePrediction getPredictionById(Long id) {
        return crimePredictionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crime prediction not found with id: " + id));
    }

    public List<CrimePrediction> getPredictionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        return crimePredictionRepository.findByPredictionDateBetween(startDate, endDate);
    }

    public List<CrimePrediction> getPredictionsByLocation(String location) {
        return crimePredictionRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<CrimePrediction> getHighRiskPredictions(Double threshold, LocalDateTime startDate, LocalDateTime endDate) {
        return crimePredictionRepository.findHighRiskPredictions(threshold, startDate, endDate);
    }

    public CrimePrediction updatePrediction(Long id, CrimePrediction updatedPrediction) {
        CrimePrediction existingPrediction = getPredictionById(id);

        existingPrediction.setCrimeType(updatedPrediction.getCrimeType());
        existingPrediction.setLocation(updatedPrediction.getLocation());
        existingPrediction.setProbability(updatedPrediction.getProbability());
        existingPrediction.setPredictionDate(updatedPrediction.getPredictionDate());
        existingPrediction.setCrimeTypeEntity(updatedPrediction.getCrimeTypeEntity());

        validatePrediction(existingPrediction);
        log.info("Updating crime prediction with id: {}", id);
        return crimePredictionRepository.save(existingPrediction);
    }

    public void deletePrediction(Long id) {
        if (!crimePredictionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Crime prediction not found with id: " + id);
        }
        log.info("Deleting crime prediction with id: {}", id);
        crimePredictionRepository.deleteById(id);
    }

    private void validatePrediction(CrimePrediction prediction) {
        if (prediction.getProbability() != null && (prediction.getProbability() < 0 || prediction.getProbability() > 1)) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }
        if (prediction.getCrimeTypeEntity() == null || prediction.getCrimeTypeEntity().getId() == null) {
            throw new IllegalArgumentException("Crime type entity must be specified");
        }
        if (!crimeTypeRepository.existsById(prediction.getCrimeTypeEntity().getId())) {
            throw new ResourceNotFoundException("Crime type not found with id: " + prediction.getCrimeTypeEntity().getId());
        }
    }
}