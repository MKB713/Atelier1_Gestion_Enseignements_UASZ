package com.uasz.daos.deroulement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiliereDTO {
    private Long id;
    private String libelle;
    private String description;
}
