package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Ec;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.ConflictException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EcRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
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
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SeanceServiceConflictTest {

    @Mock
    private SeanceRepository seanceRepository;
    @Mock
    private EnseignantRepository enseignantRepository;
    @Mock
    private SalleRepository salleRepository;
    @Mock
    private EcRepository ecRepository;

    @InjectMocks
    private SeanceService seanceService;

    private SeanceDTO seanceDTO;

    @BeforeEach
    void setUp() {
        seanceDTO = new SeanceDTO();
        seanceDTO.setDateSeance(LocalDate.now());
        seanceDTO.setHeureDebut(LocalTime.of(10, 0));
        seanceDTO.setHeureFin(LocalTime.of(12, 0));
        seanceDTO.setEnseignantId(1L);
        seanceDTO.setSalleId(1L);
        seanceDTO.setEcId(1L);
    }

    @Test
    void testCreateSeance_NoConflict() {
        // Arrange
        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(new Enseignant()));
        when(salleRepository.findById(1L)).thenReturn(Optional.of(new Salle()));
        when(ecRepository.findById(1L)).thenReturn(Optional.of(new Ec()));
        when(seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.save(any(Seance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Seance result = seanceService.createSeance(seanceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(seanceDTO.getDateSeance(), result.getDateSeance());
    }

    @Test
    void testCreateSeance_TeacherConflict() {
        // Arrange
        when(seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.createSeance(seanceDTO);
        });
        assertEquals("Conflit d'horaire : L'enseignant est déjà occupé à ce créneau.", exception.getMessage());
    }

    @Test
    void testCreateSeance_RoomConflict() {
        // Arrange
        when(seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.createSeance(seanceDTO);
        });
        assertEquals("Conflit d'horaire : La salle est déjà occupée à ce créneau.", exception.getMessage());
    }

    @Test
    void testCreateSeance_EnseignantNotFound() {
        // Arrange
        when(seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfter(any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(enseignantRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            seanceService.createSeance(seanceDTO);
        });
        assertEquals("Enseignant non trouvé avec l'id: 1", exception.getMessage());
    }

    @Test
    void testUpdateSeance_NoConflict() {
        // Arrange
        Long seanceId = 1L;
        Seance existingSeance = new Seance();
        existingSeance.setId(seanceId);

        when(seanceRepository.findById(seanceId)).thenReturn(Optional.of(existingSeance));
        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(new Enseignant()));
        when(salleRepository.findById(1L)).thenReturn(Optional.of(new Salle()));
        when(ecRepository.findById(1L)).thenReturn(Optional.of(new Ec()));
        when(seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.save(any(Seance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        seanceDTO.setHeureDebut(LocalTime.of(14, 0)); // Change time to avoid self-conflict
        Seance result = seanceService.updateSeance(seanceId, seanceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(seanceDTO.getHeureDebut(), result.getHeureDebut());
    }

    @Test
    void testUpdateSeance_TeacherConflict() {
        // Arrange
        Long seanceId = 1L;
        Seance existingSeance = new Seance();
        existingSeance.setId(seanceId);

        when(seanceRepository.findById(seanceId)).thenReturn(Optional.of(existingSeance));
        when(seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.updateSeance(seanceId, seanceDTO);
        });
        assertEquals("Conflit d'horaire : L'enseignant est déjà occupé à ce créneau.", exception.getMessage());
    }

    @Test
    void testUpdateSeance_RoomConflict() {
        // Arrange
        Long seanceId = 1L;
        Seance existingSeance = new Seance();
        existingSeance.setId(seanceId);

        when(seanceRepository.findById(seanceId)).thenReturn(Optional.of(existingSeance));
        when(seanceRepository.findByEnseignantIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(seanceRepository.findBySalleIdAndDateSeanceAndHeureDebutBeforeAndHeureFinAfterAndIdNot(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(new Seance()));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            seanceService.updateSeance(seanceId, seanceDTO);
        });
        assertEquals("Conflit d'horaire : La salle est déjà occupée à ce créneau.", exception.getMessage());
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
