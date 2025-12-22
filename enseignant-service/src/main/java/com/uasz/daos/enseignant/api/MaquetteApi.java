package com.uasz.daos.enseignant.api;

import com.uasz.daos.enseignant.dto.FormationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "maquette-service")
public interface MaquetteApi {

    @GetMapping("/api/formations")
    List<FormationDTO> getAllFormations();

    @GetMapping("/api/formations/{id}")
    FormationDTO getFormationById(@PathVariable("id") Long id);

    @GetMapping("/api/filieres/{id}")
    Object getFiliereById(@PathVariable("id") Long id);

    @GetMapping("/api/niveaux/{id}")
    Object getNiveauById(@PathVariable("id") Long id);
}
