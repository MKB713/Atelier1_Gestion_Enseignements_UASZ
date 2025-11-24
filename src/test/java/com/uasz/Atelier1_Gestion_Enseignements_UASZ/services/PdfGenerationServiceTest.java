package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.lowagie.text.DocumentException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PdfGenerationServiceTest {

    @InjectMocks
    private PdfGenerationService pdfGenerationService;

    private PlanningDTO planningDTO;

    @BeforeEach
    void setUp() {
        planningDTO = new PlanningDTO();
        planningDTO.setTitle("Test Planning");

        SeanceDTO seance1 = new SeanceDTO();
        seance1.setDateSeance(LocalDate.of(2025, 1, 10));
        seance1.setHeureDebut(LocalTime.of(9, 0));
        seance1.setHeureFin(LocalTime.of(11, 0));
        seance1.setSalleNom("Salle A101");
        seance1.setEnseignantNom("Jean Dupont");
        seance1.setEcNom("Programmation Avancée");

        SeanceDTO seance2 = new SeanceDTO();
        seance2.setDateSeance(LocalDate.of(2025, 1, 10));
        seance2.setHeureDebut(LocalTime.of(14, 0));
        seance2.setHeureFin(LocalTime.of(16, 0));
        seance2.setSalleNom("Salle A101");
        seance2.setEnseignantNom("Jean Dupont");
        seance2.setEcNom("Programmation Avancée");

        planningDTO.addSeance(LocalDate.of(2025, 1, 10), seance1);
        planningDTO.addSeance(LocalDate.of(2025, 1, 10), seance2);
    }

    @Test
    void testGeneratePdf_ReturnsNonEmptyArray() throws DocumentException {
        // Act
        byte[] pdfBytes = pdfGenerationService.generatePdf(planningDTO);

        // Assert
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        // Further validation could involve parsing the PDF or checking specific content,
        // but for a basic test, checking for non-empty is sufficient.
    }
}
