package com.uasz.daos.emploitemps.controller;

// import com.lowagie.text.DocumentException;
import com.uasz.daos.emploitemps.dto.PlanningDTO;
// import com.uasz.daos.emploitemps.service.PdfGenerationService;
import com.uasz.daos.emploitemps.service.PlanningService;
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

@RestController
@RequestMapping("/api/planning")
public class PlanningController {

    @Autowired
    private PlanningService planningService;

    // @Autowired
    // private PdfGenerationService pdfGenerationService;

    /* TODO: Implémenter PdfGenerationService pour activer cette fonctionnalité
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
    */
}
