package com.uasz.daos.deroulement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NiveauDTO {
    private Long id;
    private int numero;
    private String cycle;
}
