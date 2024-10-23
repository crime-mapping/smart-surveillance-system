package com.crimeprevention.smartsurveillancesystem.controllers;

import com.crimeprevention.smartsurveillancesystem.models.IncidentReport;
import com.crimeprevention.smartsurveillancesystem.services.IncidentReportService;
import com.crimeprevention.smartsurveillancesystem.types.EIncidentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/incident-reports")
@Slf4j
public class IncidentReportController {
    private final IncidentReportService incidentReportService;

    @Autowired
    public IncidentReportController(IncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IncidentReport> createReport(@RequestBody IncidentReport report) {
        IncidentReport created = incidentReportService.createReport(report);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentReport> getReportById(@PathVariable Long id) {
        IncidentReport report = incidentReportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<IncidentReport>> getAllReports(
            @RequestParam(required = false) EIncidentStatus status,
            @RequestParam(required = false) Long crimeTypeId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        if (status == null && crimeTypeId == null && location == null && startDate == null && endDate == null) {
            return ResponseEntity.ok(incidentReportService.getAllReports());
        }

        List<IncidentReport> reports = incidentReportService.searchReports(
                status, crimeTypeId, location, startDate, endDate
        );
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<IncidentReport>> getReportsByUser(@PathVariable Long userId) {
        List<IncidentReport> reports = incidentReportService.getReportsByUser(userId);
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentReport> updateReport(
            @PathVariable Long id,
            @RequestBody IncidentReport report
    ) {
        IncidentReport updated = incidentReportService.updateReport(id, report);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentReport> updateStatus(
            @PathVariable Long id,
            @RequestBody EIncidentStatus status
    ) {
        IncidentReport updated = incidentReportService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        incidentReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
