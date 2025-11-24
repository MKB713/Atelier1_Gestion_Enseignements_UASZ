package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnseignantServiceTest {

    @Mock
    private EnseignantRepository enseignantRepository;

    @InjectMocks
    private EnseignantService enseignantService;

    private Enseignant enseignant;
    private EnseignantUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        enseignant = new Enseignant();
        enseignant.setId(1L);
        enseignant.setMatricule(12345L);
        enseignant.setNom("Doe");
        enseignant.setPrenom("John");
        enseignant.setEmail("john.doe@example.com");
        enseignant.setTelephone("+221771234567");
        enseignant.setGrade("Professeur");
        enseignant.setStatut(Statut.PERMANENT);
        enseignant.setDateCreation(LocalDateTime.now());
        enseignant.setDateModification(LocalDateTime.now());

        updateDTO = new EnseignantUpdateDTO();
    }

//    @Test
    void testUpdateEnseignant_Success() {
        // Arrange
        updateDTO.setGrade("Maître de Conférences");
        updateDTO.setStatut(Statut.VACATAIRE);
        updateDTO.setEmail("john.updated@example.com");
        updateDTO.setTelephone("+221772345678");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));
        when(enseignantRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(enseignantRepository.save(any(Enseignant.class))).thenReturn(enseignant);

        // Act
        Enseignant result = enseignantService.updateEnseignant(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Maître de Conférences", result.getGrade());
        assertEquals(Statut.VACATAIRE, result.getStatut());
        assertEquals("john.updated@example.com", result.getEmail());
        assertEquals("+221772345678", result.getTelephone());
        assertNotNull(result.getDateModification());
        verify(enseignantRepository, times(1)).findById(1L);
        verify(enseignantRepository, times(1)).save(enseignant);
    }

//    @Test
    void testUpdateEnseignant_NotFound() {
        // Arrange
        when(enseignantRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enseignantService.updateEnseignant(999L, updateDTO);
        });

        assertEquals("Enseignant non trouvé avec l'id: 999", exception.getMessage());
        verify(enseignantRepository, times(1)).findById(999L);
        verify(enseignantRepository, never()).save(any(Enseignant.class));
    }

//    @Test
    void testUpdateEnseignant_OnlyGrade() {
        // Arrange
        updateDTO.setGrade("Maître Assistant");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));
        when(enseignantRepository.save(any(Enseignant.class))).thenReturn(enseignant);

        // Act
        Enseignant result = enseignantService.updateEnseignant(1L, updateDTO);

        // Assert
        assertEquals("Maître Assistant", result.getGrade());
        assertEquals(Statut.PERMANENT, result.getStatut()); // Non modifié
        assertEquals("john.doe@example.com", result.getEmail()); // Non modifié
        verify(enseignantRepository, times(1)).save(enseignant);
    }

//    @Test
    void testUpdateEnseignant_OnlyEmail() {
        // Arrange
        updateDTO.setEmail("new.email@example.com");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));
        when(enseignantRepository.findByEmail("new.email@example.com")).thenReturn(Optional.empty());
        when(enseignantRepository.save(any(Enseignant.class))).thenReturn(enseignant);

        // Act
        Enseignant result = enseignantService.updateEnseignant(1L, updateDTO);

        // Assert
        assertEquals("new.email@example.com", result.getEmail());
        assertEquals("Professeur", result.getGrade()); // Non modifié
        verify(enseignantRepository, times(1)).findByEmail("new.email@example.com");
    }

//    @Test
    void testUpdateEnseignant_EmailAlreadyUsed() {
        // Arrange
        updateDTO.setEmail("existing@example.com");
        Enseignant autreEnseignant = new Enseignant();
        autreEnseignant.setId(2L);
        autreEnseignant.setEmail("existing@example.com");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));
        when(enseignantRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(autreEnseignant));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enseignantService.updateEnseignant(1L, updateDTO);
        });

        assertEquals("Cet email est déjà utilisé par un autre enseignant", exception.getMessage());
        verify(enseignantRepository, never()).save(any(Enseignant.class));
    }

//    @Test
    void testUpdateEnseignant_InvalidEmail() {
        // Arrange
        updateDTO.setEmail("invalid-email");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            enseignantService.updateEnseignant(1L, updateDTO);
        });

        assertEquals("Format d'email invalide", exception.getMessage());
        verify(enseignantRepository, never()).save(any(Enseignant.class));
    }

//    @Test
    void testUpdateEnseignant_InvalidTelephone() {
        // Arrange
        updateDTO.setTelephone("invalid-phone");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            enseignantService.updateEnseignant(1L, updateDTO);
        });

        assertEquals("Format de téléphone invalide", exception.getMessage());
        verify(enseignantRepository, never()).save(any(Enseignant.class));
    }

//    @Test
    void testUpdateEnseignant_OnlyStatut() {
        // Arrange
        updateDTO.setStatut(Statut.VACATAIRE);

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));
        when(enseignantRepository.save(any(Enseignant.class))).thenReturn(enseignant);

        // Act
        Enseignant result = enseignantService.updateEnseignant(1L, updateDTO);

        // Assert
        assertEquals(Statut.VACATAIRE, result.getStatut());
        assertEquals("Professeur", result.getGrade()); // Non modifié
        verify(enseignantRepository, times(1)).save(enseignant);
    }

//    @Test
    void testUpdateEnseignant_DateModificationUpdated() {
        // Arrange
        LocalDateTime beforeUpdate = LocalDateTime.now().minusDays(1);
        enseignant.setDateModification(beforeUpdate);
        updateDTO.setGrade("Nouveau Grade");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));
        when(enseignantRepository.save(any(Enseignant.class))).thenAnswer(invocation -> {
            Enseignant saved = invocation.getArgument(0);
            assertTrue(saved.getDateModification().isAfter(beforeUpdate));
            return saved;
        });

        // Act
        enseignantService.updateEnseignant(1L, updateDTO);

        // Assert
        verify(enseignantRepository, times(1)).save(enseignant);
    }
}

