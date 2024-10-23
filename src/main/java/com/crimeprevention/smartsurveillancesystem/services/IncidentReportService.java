package com.crimeprevention.smartsurveillancesystem.services;

import com.crimeprevention.smartsurveillancesystem.models.IncidentReport;
import com.crimeprevention.smartsurveillancesystem.repositories.CrimeTypeRepository;
import com.crimeprevention.smartsurveillancesystem.repositories.IncidentReportRepository;
import com.crimeprevention.smartsurveillancesystem.repositories.UserRepository;
import com.crimeprevention.smartsurveillancesystem.types.EIncidentStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class IncidentReportService {
    private final IncidentReportRepository incidentReportRepository;
    private final UserRepository userRepository;
    private final CrimeTypeRepository crimeTypeRepository;

    @Autowired
    public IncidentReportService(
            IncidentReportRepository incidentReportRepository,
            UserRepository userRepository,
            CrimeTypeRepository crimeTypeRepository
    ) {
        this.incidentReportRepository = incidentReportRepository;
        this.userRepository = userRepository;
        this.crimeTypeRepository = crimeTypeRepository;
    }

    public IncidentReport createReport(IncidentReport report) {
        validateReport(report);
        if (report.getTimestamp() == null) {
            report.setTimestamp(LocalDateTime.now());
        }
        if (report.getStatus() == null) {
            report.setStatus(EIncidentStatus.PENDING);
        }
        log.info("Creating new incident report: {}", report.getTitle());
        return incidentReportRepository.save(report);
    }

    public IncidentReport getReportById(Long id) {
        return incidentReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident report not found with id: " + id));
    }

    public List<IncidentReport> getAllReports() {
        return incidentReportRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    public List<IncidentReport> getReportsByStatus(EIncidentStatus status) {
        return incidentReportRepository.findByStatusOrderByTimestampDesc(status);
    }

    public List<IncidentReport> getReportsByUser(Long userId) {
        return incidentReportRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public List<IncidentReport> getReportsByLocation(String location) {
        return incidentReportRepository.findByLocationContainingIgnoreCaseOrderByTimestampDesc(location);
    }

    public List<IncidentReport> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        return incidentReportRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate);
    }

    public List<IncidentReport> searchReports(
            EIncidentStatus status,
            Long crimeTypeId,
            String location,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        if (startDate == null) startDate = LocalDateTime.now().minusMonths(1);
        if (endDate == null) endDate = LocalDateTime.now();

        return incidentReportRepository.findByAdvancedCriteria(
                status, crimeTypeId, location, startDate, endDate
        );
    }

    public IncidentReport updateReport(Long id, IncidentReport updatedReport) {
        IncidentReport existingReport = getReportById(id);

        existingReport.setTitle(updatedReport.getTitle());
        existingReport.setDescription(updatedReport.getDescription());
        existingReport.setLocation(updatedReport.getLocation());
        existingReport.setStatus(updatedReport.getStatus());
        existingReport.setCrimeType(updatedReport.getCrimeType());

        validateReport(existingReport);
        log.info("Updating incident report with id: {}", id);
        return incidentReportRepository.save(existingReport);
    }

    public IncidentReport updateStatus(Long id, EIncidentStatus newStatus) {
        IncidentReport report = getReportById(id);
        report.setStatus(newStatus);
        log.info("Updating status of incident report {} to {}", id, newStatus);
        return incidentReportRepository.save(report);
    }

    public void deleteReport(Long id) {
        if (!incidentReportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Incident report not found with id: " + id);
        }
        log.info("Deleting incident report with id: {}", id);
        incidentReportRepository.deleteById(id);
    }

    private void validateReport(IncidentReport report) {
        if (report.getUser() == null || report.getUser().getId() == null) {
            throw new IllegalArgumentException("User must be specified");
        }
        if (!userRepository.existsById(report.getUser().getId())) {
            throw new ResourceNotFoundException("User not found with id: " + report.getUser().getId());
        }
        if (report.getCrimeType() != null && !crimeTypeRepository.existsById(report.getCrimeType().getId())) {
            throw new ResourceNotFoundException("Crime type not found with id: " + report.getCrimeType().getId());
        }
    }
}
