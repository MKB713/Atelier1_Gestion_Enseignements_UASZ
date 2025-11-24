package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.EnseignantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnseignantController.class)
class EnseignantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnseignantService enseignantService;

    @Autowired
    private ObjectMapper objectMapper;

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
        updateDTO.setGrade("Maître de Conférences");
        updateDTO.setStatut(Statut.VACATAIRE);
        updateDTO.setEmail("john.updated@example.com");
        updateDTO.setTelephone("+221772345678");
    }

//    @Test
//    void testUpdateEnseignant_Success() throws Exception {
//        // Arrange
//        Enseignant updatedEnseignant = new Enseignant();
//        updatedEnseignant.setId(1L);
//        updatedEnseignant.setGrade(updateDTO.getGrade());
//        updatedEnseignant.setStatut(updateDTO.getStatut());
//        updatedEnseignant.setEmail(updateDTO.getEmail());
//        updatedEnseignant.setTelephone(updateDTO.getTelephone());
//
//        when(enseignantService.updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class)))
//                .thenReturn(updatedEnseignant);
//
//        // Act & Assert
//        mockMvc.perform(put("/api/enseignants/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.grade").value("Maître de Conférences"))
//                .andExpect(jsonPath("$.statut").value("VACATAIRE"))
//                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
//                .andExpect(jsonPath("$.telephone").value("+221772345678"));
//
//        verify(enseignantService, times(1)).updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class));
//    }
//
//    @Test
//    void testUpdateEnseignant_NotFound() throws Exception {
//        // Arrange
//        when(enseignantService.updateEnseignant(eq(999L), any(EnseignantUpdateDTO.class)))
//                .thenThrow(new RuntimeException("Enseignant non trouvé avec l'id: 999"));
//
//        // Act & Assert
//        mockMvc.perform(put("/api/enseignants/999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDTO)))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Enseignant non trouvé avec l'id: 999"));
//
//        verify(enseignantService, times(1)).updateEnseignant(eq(999L), any(EnseignantUpdateDTO.class));
//    }
//
//    @Test
//    void testUpdateEnseignant_InvalidEmail() throws Exception {
//        // Arrange
//        updateDTO.setEmail("invalid-email");
//        when(enseignantService.updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class)))
//                .thenThrow(new IllegalArgumentException("Format d'email invalide"));
//
//        // Act & Assert
//        mockMvc.perform(put("/api/enseignants/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("Erreur de validation")));
//
//        verify(enseignantService, times(1)).updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class));
//    }
//
//    @Test
//    void testUpdateEnseignant_EmailAlreadyUsed() throws Exception {
//        // Arrange
//        when(enseignantService.updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class)))
//                .thenThrow(new RuntimeException("Cet email est déjà utilisé par un autre enseignant"));
//
//        // Act & Assert
//        mockMvc.perform(put("/api/enseignants/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Cet email est déjà utilisé par un autre enseignant"));
//
//        verify(enseignantService, times(1)).updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class));
//    }
//
//    @Test
//    void testUpdateEnseignant_OnlyGrade() throws Exception {
//        // Arrange
//        EnseignantUpdateDTO partialUpdate = new EnseignantUpdateDTO();
//        partialUpdate.setGrade("Maître Assistant");
//
//        Enseignant updatedEnseignant = new Enseignant();
//        updatedEnseignant.setId(1L);
//        updatedEnseignant.setGrade("Maître Assistant");
//        updatedEnseignant.setStatut(Statut.PERMANENT);
//        updatedEnseignant.setEmail("john.doe@example.com");
//
//        when(enseignantService.updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class)))
//                .thenReturn(updatedEnseignant);
//
//        // Act & Assert
//        mockMvc.perform(put("/api/enseignants/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(partialUpdate)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.grade").value("Maître Assistant"));
//
//        verify(enseignantService, times(1)).updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class));
//    }
//
//    @Test
//    void testUpdateEnseignant_InvalidTelephone() throws Exception {
//        // Arrange
//        updateDTO.setTelephone("invalid-phone");
//        when(enseignantService.updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class)))
//                .thenThrow(new IllegalArgumentException("Format de téléphone invalide"));
//
//        // Act & Assert
//        mockMvc.perform(put("/api/enseignants/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("Erreur de validation")));
//
//        verify(enseignantService, times(1)).updateEnseignant(eq(1L), any(EnseignantUpdateDTO.class));
//    }
//
//    @Test
//    void testUpdateEnseignant_ValidationError() throws Exception {
//        // Arrange - Email invalide selon les annotations de validation
//        updateDTO.setEmail("not-an-email");
//
//        // Act & Assert
//        mockMvc.perform(put("/api/enseignants/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDTO)))
//                .andExpect(status().isBadRequest());
//
//        // Le service ne devrait pas être appelé si la validation échoue
//        verify(enseignantService, never()).updateEnseignant(anyLong(), any(EnseignantUpdateDTO.class));
//    }
}


