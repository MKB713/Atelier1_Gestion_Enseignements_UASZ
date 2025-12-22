package com.uasz.daos.enseignant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormationDTO {
    private Long id;
    private String code;
    private String libelle;
    private String description;
    private Date dateCreation;
    private String statutFormation;
}
