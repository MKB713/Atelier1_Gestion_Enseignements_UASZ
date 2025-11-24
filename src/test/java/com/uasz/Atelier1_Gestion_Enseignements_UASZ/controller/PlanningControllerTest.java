package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.lowagie.text.DocumentException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.PdfGenerationService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.PlanningService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlanningController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class PlanningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanningService planningService;

    @MockBean
    private PdfGenerationService pdfGenerationService;

    private PlanningDTO planningDTO;
    private byte[] pdfBytes;

    @BeforeEach
    void setUp() throws DocumentException {
        planningDTO = new PlanningDTO();
        planningDTO.setTitle("Test Planning");

        pdfBytes = "test pdf content".getBytes(); // Simulate PDF content
        when(pdfGenerationService.generatePdf(any(PlanningDTO.class))).thenReturn(pdfBytes);
    }

    @Test
    void testGeneratePlanningPdf_Success() throws Exception {
        // Arrange
        Long salleId = 1L;
        when(planningService.getPlanningBySalle(salleId)).thenReturn(planningDTO);

        // Act & Assert
        mockMvc.perform(get("/api/planning/pdf")
                        .param("salleId", salleId.toString())
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"planning_salle_1.pdf\""))
                .andExpect(content().bytes(pdfBytes));
    }

    @Test
    void testGeneratePlanningPdf_SalleNotFound() throws Exception {
        // Arrange
        Long salleId = 99L;
        when(planningService.getPlanningBySalle(salleId))
                .thenThrow(new EntityNotFoundException("Salle non trouvée avec l'id: " + salleId));

        // Act & Assert
        mockMvc.perform(get("/api/planning/pdf")
                        .param("salleId", salleId.toString())
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGeneratePlanningPdf_GenerationError() throws Exception {
        // Arrange
        Long salleId = 1L;
        when(planningService.getPlanningBySalle(salleId)).thenReturn(planningDTO);
        when(pdfGenerationService.generatePdf(any(PlanningDTO.class)))
                .thenThrow(new DocumentException("PDF generation failed"));

        // Act & Assert
        mockMvc.perform(get("/api/planning/pdf")
                        .param("salleId", salleId.toString())
                        .accept(MediaType.APPLICATION_PDF))
                .andExpect(status().isInternalServerError());
    }
}
