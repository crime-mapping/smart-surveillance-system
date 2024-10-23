package com.crimeprevention.smartsurveillancesystem.repositories;

import com.crimeprevention.smartsurveillancesystem.models.CrimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CrimeTypeRepository extends JpaRepository<CrimeType, Long> {
    Optional<CrimeType> findByNameIgnoreCase(String name);
    List<CrimeType> findByNameContainingIgnoreCase(String namePattern);
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT ct FROM CrimeType ct LEFT JOIN FETCH ct.incidentReports WHERE ct.id = :id")
    Optional<CrimeType> findByIdWithIncidentReports(@Param("id") Long id);

    @Query("SELECT ct FROM CrimeType ct LEFT JOIN FETCH ct.crimes WHERE ct.id = :id")
    Optional<CrimeType> findByIdWithCrimes(@Param("id") Long id);

    @Query("SELECT DISTINCT ct FROM CrimeType ct " +
            "LEFT JOIN ct.incidentReports ir " +
            "GROUP BY ct.id, ct.name " +
            "ORDER BY COUNT(ir) DESC")
    List<CrimeType> findMostReportedCrimeTypes(Pageable pageable);
}
