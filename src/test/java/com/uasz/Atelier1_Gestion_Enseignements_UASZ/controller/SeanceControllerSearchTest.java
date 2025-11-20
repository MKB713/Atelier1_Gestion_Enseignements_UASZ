/*
package com.uasz.Atelier1_Gestion_Enseignements_UASZ.controller;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SeanceController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class SeanceControllerSearchTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeanceService seanceService;

    private Seance seance1;
    private Enseignant enseignant;

    @BeforeEach
    void setUp() {
        enseignant = new Enseignant();
        enseignant.setId(1L);
        enseignant.setNom("Doe");

        seance1 = new Seance();
        seance1.setId(1L);
        seance1.setDateSeance(LocalDate.now());
        seance1.setHeureDebut(LocalTime.of(8, 0));
        seance1.setHeureFin(LocalTime.of(10, 0));
        seance1.setEnseignant(enseignant);
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