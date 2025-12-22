package com.uasz.daos.emploitemps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ECDTO {
    private Long id;
    private String code;
    private String libelle;
    private int credit;
    private int heureCm;
    private int heureTd;
    private int heureTp;
    private double coefficient;
    private int tpe;
}
