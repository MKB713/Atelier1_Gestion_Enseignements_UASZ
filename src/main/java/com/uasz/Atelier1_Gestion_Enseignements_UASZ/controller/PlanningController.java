package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.lowagie.text.DocumentException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.PdfGenerationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.PlanningService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.SeanceService; // New import
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter; // New import
import java.util.List; // New import

@RestController
@RequestMapping("/api/planning")
public class PlanningController {

    @Autowired
    private PlanningService planningService;

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @Autowired // New injection
    private SeanceService seanceService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePlanningPdf(@RequestParam Long salleId) {
        try {
            PlanningDTO planning = planningService.getPlanningBySalle(salleId);
            byte[] pdfBytes = pdfGenerationService.generatePdf(planning);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "planning_salle_" + salleId + ".pdf";
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.NOT_FOUND);
        } catch (DocumentException e) {
            return new ResponseEntity<>(("Error generating PDF: " + e.getMessage()).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pdf/seances")
    public ResponseEntity<byte[]> generateSeancesPdf(
            @RequestParam(name = "startDate", required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
            @RequestParam(name = "weekDate", required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate weekDate) {
        try {
            List<Seance> seances;
            String title;

            if (weekDate != null) {
                seances = seanceService.getSeancesByWeek(weekDate);
                java.time.LocalDate monday = weekDate.with(java.time.DayOfWeek.MONDAY);
                title = "Planning Hebdomadaire du " + monday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else if (startDate != null && endDate != null) {
                seances = seanceService.getSeancesByDateRange(startDate, endDate);
                title = "Planning du " + startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " au " + endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                return new ResponseEntity<>("Missing date parameters for PDF generation.".getBytes(), HttpStatus.BAD_REQUEST);
            }

            byte[] pdfBytes = pdfGenerationService.generatePdf(seances, title); // Signature change needed in PdfGenerationService

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = title.replace(" ", "_") + ".pdf";
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) { // Catch generic Exception for now
            return new ResponseEntity<>(("Error generating PDF: " + e.getMessage()).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
