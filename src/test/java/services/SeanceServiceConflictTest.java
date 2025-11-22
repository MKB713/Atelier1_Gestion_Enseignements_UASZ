package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.ConflictException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeanceServiceConflictTest {

    @Mock
    private SeanceRepository seanceRepository;
    @Mock
    private SalleRepository salleRepository;
    @Mock
    private EmploiRepository emploiRepository;
    @Mock
    private RepartitionRepository repartitionRepository;
    @Mock
    private DeroulementRepository deroulementRepository;

    @InjectMocks
    private SeanceService seanceService;

    private SeanceDTO seanceDTO;
    private Salle salle;
    private Enseignant enseignant;
    private Ec ec;
    private Emploi emploi;
    private Repartition repartition;

    @BeforeEach
    void setUp() {
        salle = new Salle();
        salle.setId(1L);
        salle.setLibelle("Salle A");

        enseignant = new Enseignant();
        enseignant.setId(10L);
        enseignant.setNom("Doe");
        enseignant.setPrenom("John");

        ec = new Ec();
        ec.setId(100L);
        ec.setLibelle("Maths");

        emploi = new Emploi();
        emploi.setId(1L);
        emploi.setLibelle("Emploi du temps L1");

        repartition = new Repartition();
        repartition.setId(1L);
        repartition.setEnseignant(enseignant);
        repartition.setEc(ec);

        seanceDTO = new SeanceDTO();
        seanceDTO.setDateSeance(LocalDate.now());
        seanceDTO.setHeureDebut(LocalTime.of(10, 0));
        seanceDTO.setHeureFin(LocalTime.of(12, 0));
        seanceDTO.setDuree(120);
        seanceDTO.setSalleId(salle.getId());
        seanceDTO.setEmploiId(emploi.getId());
        seanceDTO.setRepartitionId(repartition.getId());
    }

    @Test
    void testCreateSeance_NoConflict() {
        // Arrange
        when(salleRepository.findById(salle.getId())).thenReturn(Optional.of(salle));
        when(emploiRepository.findById(emploi.getId())).thenReturn(Optional.of(emploi));
        when(repartitionRepository.findById(repartition.getId())).thenReturn(Optional.of(repartition));
        when(seanceRepository.findTeacherConflicts(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(deroulementRepository.save(any(Deroulement.class))).thenReturn(new Deroulement());
        when(seanceRepository.save(any(Seance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Seance result = seanceService.createSeance(seanceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(seanceDTO.getDateSeance(), result.getDateSeance());
        verify(seanceRepository, times(1)).save(any(Seance.class));
    }

    @Test
    void testCreateSeance_TeacherConflict() {
        // Arrange
        when(repartitionRepository.findById(repartition.getId())).thenReturn(Optional.of(repartition));
        when(seanceRepository.findTeacherConflicts(any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.createSeance(seanceDTO);
        });
        assertEquals("Conflit d'horaire : L'enseignant est déjà occupé à ce créneau.", exception.getMessage());
        verify(seanceRepository, never()).save(any(Seance.class));
    }

    @Test
    void testCreateSeance_RoomConflict() {
        // Arrange
        when(repartitionRepository.findById(repartition.getId())).thenReturn(Optional.of(repartition));
        when(seanceRepository.findTeacherConflicts(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.createSeance(seanceDTO);
        });
        assertEquals("Conflit d'horaire : La salle est déjà occupée à ce créneau.", exception.getMessage());
        verify(seanceRepository, never()).save(any(Seance.class));
    }

    @Test
    void testCreateSeance_RepartitionNotFound() {
        // Arrange
        when(repartitionRepository.findById(repartition.getId())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            seanceService.createSeance(seanceDTO);
        });
        assertEquals("Repartition non trouvée avec l'id: " + repartition.getId(), exception.getMessage());
        verify(seanceRepository, never()).save(any(Seance.class));
    }

    @Test
    void testUpdateSeance_NoConflict() {
        // Arrange
        Long seanceId = 1L;
        Seance existingSeance = new Seance();
        existingSeance.setId(seanceId);
        existingSeance.setRepartition(repartition); // Important for conflict check

        when(seanceRepository.findById(seanceId)).thenReturn(Optional.of(existingSeance));
        when(salleRepository.findById(salle.getId())).thenReturn(Optional.of(salle));
        when(emploiRepository.findById(emploi.getId())).thenReturn(Optional.of(emploi));
        when(repartitionRepository.findById(repartition.getId())).thenReturn(Optional.of(repartition));
        when(seanceRepository.findTeacherConflictsExcludingId(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.save(any(Seance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        seanceDTO.setHeureDebut(LocalTime.of(14, 0)); // Change time to avoid self-conflict
        Seance result = seanceService.updateSeance(seanceId, seanceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(seanceDTO.getHeureDebut(), result.getHeureDebut());
        verify(seanceRepository, times(1)).save(any(Seance.class));
    }

    @Test
    void testUpdateSeance_TeacherConflict() {
        // Arrange
        Long seanceId = 1L;
        Seance existingSeance = new Seance();
        existingSeance.setId(seanceId);
        existingSeance.setRepartition(repartition);

        when(seanceRepository.findById(seanceId)).thenReturn(Optional.of(existingSeance));
        when(repartitionRepository.findById(repartition.getId())).thenReturn(Optional.of(repartition));
        when(seanceRepository.findTeacherConflictsExcludingId(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.updateSeance(seanceId, seanceDTO);
        });
        assertEquals("Conflit d'horaire : L'enseignant est déjà occupé à ce créneau.", exception.getMessage());
        verify(seanceRepository, never()).save(any(Seance.class));
    }

    @Test
    void testUpdateSeance_RoomConflict() {
        // Arrange
        Long seanceId = 1L;
        Seance existingSeance = new Seance();
        existingSeance.setId(seanceId);
        existingSeance.setRepartition(repartition);

        when(seanceRepository.findById(seanceId)).thenReturn(Optional.of(existingSeance));
        when(repartitionRepository.findById(repartition.getId())).thenReturn(Optional.of(repartition));
        when(seanceRepository.findTeacherConflictsExcludingId(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.updateSeance(seanceId, seanceDTO);
        });
        assertEquals("Conflit d'horaire : La salle est déjà occupée à ce créneau.", exception.getMessage());
        verify(seanceRepository, never()).save(any(Seance.class));
    }

    @Test
    void testUpdateSeance_NotFound() {
        // Arrange
        Long seanceId = 99L;
        when(seanceRepository.findById(seanceId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            seanceService.updateSeance(seanceId, seanceDTO);
        });
        assertEquals("Séance non trouvée avec l'id: 99", exception.getMessage());
        verify(seanceRepository, never()).save(any(Seance.class));
    }

    @Test
    void testDeleteSeance_Success() {
        // Arrange
        Long seanceId = 1L;
        when(seanceRepository.existsById(seanceId)).thenReturn(true);

        // Act
        seanceService.deleteSeance(seanceId);

        // Assert
        verify(seanceRepository, times(1)).deleteById(seanceId);
    }

    @Test
    void testDeleteSeance_NotFound() {
        // Arrange
        Long seanceId = 99L;
        when(seanceRepository.existsById(seanceId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            seanceService.deleteSeance(seanceId);
        });
        assertEquals("Séance non trouvée avec l'id: 99", exception.getMessage());
        verify(seanceRepository, never()).deleteById(any());
    }
}
