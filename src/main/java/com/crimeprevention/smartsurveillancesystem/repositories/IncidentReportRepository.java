package com.crimeprevention.smartsurveillancesystem.repositories;

import com.crimeprevention.smartsurveillancesystem.models.IncidentReport;
import com.crimeprevention.smartsurveillancesystem.types.EIncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    List<IncidentReport> findByStatusOrderByTimestampDesc(EIncidentStatus status);
    List<IncidentReport> findByUserIdOrderByTimestampDesc(Long userId);
    List<IncidentReport> findByCrimeTypeIdOrderByTimestampDesc(Long crimeTypeId);
    List<IncidentReport> findByLocationContainingIgnoreCaseOrderByTimestampDesc(String location);
    List<IncidentReport> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT ir FROM IncidentReport ir WHERE " +
            "(:status IS NULL OR ir.status = :status) AND " +
            "(:crimeTypeId IS NULL OR ir.crimeType.id = :crimeTypeId) AND " +
            "(:location IS NULL OR LOWER(ir.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "ir.timestamp BETWEEN :startDate AND :endDate")
    List<IncidentReport> findByAdvancedCriteria(
            @Param("status") EIncidentStatus status,
            @Param("crimeTypeId") Long crimeTypeId,
            @Param("location") String location,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(ir) FROM IncidentReport ir WHERE ir.crimeType.id = :crimeTypeId AND " +
            "ir.timestamp BETWEEN :startDate AND :endDate")
    long countByCrimeTypeAndDateRange(
            @Param("crimeTypeId") Long crimeTypeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
