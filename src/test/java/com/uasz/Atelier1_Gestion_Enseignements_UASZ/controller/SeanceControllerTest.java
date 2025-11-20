/*
package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Salle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.ConflictException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.SeanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SeanceController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class SeanceControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeanceService seanceService;

    @Autowired
    private ObjectMapper objectMapper;

    private Seance seance1;
    private Seance seance2;
    private Salle salle;
    private Enseignant enseignant;
    private SeanceDTO seanceDTO;

    @BeforeEach
    void setUp() {
        salle = new Salle();
        salle.setId(1L);
        salle.setLibelle("Salle 101");

        enseignant = new Enseignant();
        enseignant.setId(1L);
        enseignant.setNom("Doe");

        seance1 = new Seance();
        seance1.setId(1L);
        seance1.setDateSeance(LocalDate.now());
        seance1.setHeureDebut(LocalTime.of(8, 0));
        seance1.setHeureFin(LocalTime.of(10, 0));
        seance1.setSalle(salle);
        seance1.setEnseignant(enseignant);

        seance2 = new Seance();
        seance2.setId(2L);
        seance2.setDateSeance(LocalDate.now().plusDays(1));
        seance2.setHeureDebut(LocalTime.of(10, 0));
        seance2.setHeureFin(LocalTime.of(12, 0));
        seance2.setSalle(salle);
        seance2.setEnseignant(enseignant);

        seanceDTO = new SeanceDTO();
        seanceDTO.setDateSeance(LocalDate.now());
        seanceDTO.setHeureDebut(LocalTime.of(10, 0));
        seanceDTO.setHeureFin(LocalTime.of(12, 0));
        seanceDTO.setEnseignantId(1L);
        seanceDTO.setSalleId(1L);
        seanceDTO.setEcId(1L);
    }

    @Test
    void testGetSeancesByEnseignant() throws Exception {
        // Arrange
        List<Seance> seances = new ArrayList<>();
        seances.add(seance1);

        when(seanceService.getByEnseignant(1L)).thenReturn(seances);

        // Act & Assert
        mockMvc.perform(get("/api/seances/enseignant/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].enseignant.nom").value("Doe"));
    }
}
*/