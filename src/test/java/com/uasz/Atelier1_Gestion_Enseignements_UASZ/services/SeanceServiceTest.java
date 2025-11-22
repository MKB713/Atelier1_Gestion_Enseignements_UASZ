package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Ec;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Emploi;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Repartition;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.SeanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeanceServiceTest {

    @Mock
    private SeanceRepository seanceRepository;

    @InjectMocks
    private SeanceService seanceService;

    private Seance seance1;
    private Seance seance2;
    private Salle salle;
    private Enseignant enseignant;
    private Ec ec;
    private Emploi emploi;
    private Repartition repartition;

    @BeforeEach
    void setUp() {
        salle = new Salle();
        salle.setId(1L);
        salle.setLibelle("Salle 101");

        enseignant = new Enseignant();
        enseignant.setId(1L);
        enseignant.setNom("Doe");
        enseignant.setPrenom("John");

        ec = new Ec();
        ec.setId(100L);
        ec.setLibelle("Matiere Test");

        emploi = new Emploi();
        emploi.setId(1L);
        emploi.setLibelle("Emploi Test");

        repartition = new Repartition();
        repartition.setId(1L);
        repartition.setEnseignant(enseignant);
        repartition.setEc(ec);

        seance1 = new Seance();
        seance1.setId(1L);
        seance1.setDateSeance(LocalDate.now());
        seance1.setHeureDebut(LocalTime.of(8, 0));
        seance1.setHeureFin(LocalTime.of(10, 0));
        seance1.setSalle(salle);
        seance1.setRepartition(repartition);
        seance1.setEmploi(emploi);
        seance1.setDuree(120);

        seance2 = new Seance();
        seance2.setId(2L);
        seance2.setDateSeance(LocalDate.now().plusDays(1));
        seance2.setHeureDebut(LocalTime.of(10, 0));
        seance2.setHeureFin(LocalTime.of(12, 0));
        seance2.setSalle(salle);
        seance2.setRepartition(repartition);
        seance2.setEmploi(emploi);
        seance2.setDuree(120);
    }

    @Test
    void testGetBySalle() {
        // Arrange
        List<Seance> seances = new ArrayList<>();
        seances.add(seance1);
        seances.add(seance2);

        when(seanceRepository.findBySalleId(1L)).thenReturn(seances);

        // Act
        List<Seance> result = seanceService.getBySalle(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(seance1, result.get(0));
        assertEquals(seance2, result.get(1));
        verify(seanceRepository, times(1)).findBySalleId(1L);
    }

    @Test
    void testGetByEnseignant() {
        // Arrange
        List<Seance> seances = new ArrayList<>();
        seances.add(seance1);
        seances.add(seance2);

        when(seanceRepository.findByEnseignantId(1L)).thenReturn(seances);

        // Act
        List<Seance> result = seanceService.getByEnseignant(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(seance1, result.get(0));
        assertEquals(seance2, result.get(1));
        verify(seanceRepository, times(1)).findByEnseignantId(1L);
    }
}
