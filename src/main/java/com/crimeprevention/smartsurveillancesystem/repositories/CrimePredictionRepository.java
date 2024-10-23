package com.crimeprevention.smartsurveillancesystem.repositories;

import com.crimeprevention.smartsurveillancesystem.models.CrimePrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CrimePredictionRepository extends JpaRepository<CrimePrediction, Long> {
    List<CrimePrediction> findByPredictionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<CrimePrediction> findByCrimeType(String crimeType);
    List<CrimePrediction> findByLocationContainingIgnoreCase(String location);
    List<CrimePrediction> findByProbabilityGreaterThan(Double threshold);
    List<CrimePrediction> findByCrimeTypeEntityId(Long crimeTypeId);

    @Query("SELECT cp FROM CrimePrediction cp WHERE cp.probability >= :threshold " +
            "AND cp.predictionDate BETWEEN :startDate AND :endDate")
    List<CrimePrediction> findHighRiskPredictions(
            @Param("threshold") Double threshold,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
