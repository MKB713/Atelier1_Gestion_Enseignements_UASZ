package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Ec;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Repartition; // New import
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SalleRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SeanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanningServiceTest {

    @Mock
    private SeanceRepository seanceRepository;

    @Mock
    private SalleRepository salleRepository;

    @InjectMocks
    private PlanningService planningService;

    private Salle salle;
    private Enseignant enseignant;
    private Ec ec;
    private Seance seance1;
    private Seance seance2;

    @BeforeEach
    void setUp() {
        salle = new Salle();
        salle.setId(1L);
        salle.setLibelle("Salle A101");

        enseignant = new Enseignant();
        enseignant.setId(10L);
        enseignant.setNom("Dupont");
        enseignant.setPrenom("Jean");

        ec = new Ec();
        ec.setId(100L);
        ec.setLibelle("Programmation Avancée");

        // Create Repartition objects
        Repartition repartition1 = new Repartition();
        repartition1.setId(1L);
        repartition1.setEnseignant(enseignant);
        repartition1.setEc(ec);

        Repartition repartition2 = new Repartition();
        repartition2.setId(2L);
        repartition2.setEnseignant(enseignant);
        repartition2.setEc(ec);

        seance1 = new Seance();
        seance1.setId(1L);
        seance1.setDateSeance(LocalDate.of(2025, 1, 10));
        seance1.setHeureDebut(LocalTime.of(9, 0));
        seance1.setHeureFin(LocalTime.of(11, 0));
        seance1.setSalle(salle);
        seance1.setRepartition(repartition1); // Set Repartition
        seance1.setDuree(120); // Add duree

        seance2 = new Seance();
        seance2.setId(2L);
        seance2.setDateSeance(LocalDate.of(2025, 1, 10));
        seance2.setHeureDebut(LocalTime.of(14, 0));
        seance2.setHeureFin(LocalTime.of(16, 0));
        seance2.setSalle(salle);
        seance2.setRepartition(repartition2); // Set Repartition
        seance2.setDuree(120); // Add duree
    }

    @Test
    void testGetPlanningBySalle_Success() {
        // Arrange
        when(salleRepository.findById(1L)).thenReturn(Optional.of(salle));
        when(seanceRepository.findBySalleId(1L)).thenReturn(Arrays.asList(seance1, seance2));

        // Act
        PlanningDTO result = planningService.getPlanningBySalle(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Emploi du temps pour la salle : Salle A101", result.getTitle());
        assertEquals(1, result.getSchedule().size()); // One day
        assertTrue(result.getSchedule().containsKey(LocalDate.of(2025, 1, 10)));
        assertEquals(2, result.getSchedule().get(LocalDate.of(2025, 1, 10)).size());
        assertEquals("Jean Dupont", result.getSchedule().get(LocalDate.of(2025, 1, 10)).get(0).getEnseignantNom());
        assertEquals("Programmation Avancée", result.getSchedule().get(LocalDate.of(2025, 1, 10)).get(0).getEcLibelle()); // Changed from getEcNom()
    }

    @Test
    void testGetPlanningBySalle_SalleNotFound() {
        // Arrange
        when(salleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            planningService.getPlanningBySalle(99L);
        });
        assertEquals("Salle non trouvée avec l'id: 99", exception.getMessage());
    }

    @Test
    void testGetPlanningBySalle_NoSeances() {
        // Arrange
        when(salleRepository.findById(1L)).thenReturn(Optional.of(salle));
        when(seanceRepository.findBySalleId(1L)).thenReturn(Collections.emptyList());

        // Act
        PlanningDTO result = planningService.getPlanningBySalle(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Emploi du temps pour la salle : Salle A101", result.getTitle());
        assertTrue(result.getSchedule().isEmpty());
    }
}
