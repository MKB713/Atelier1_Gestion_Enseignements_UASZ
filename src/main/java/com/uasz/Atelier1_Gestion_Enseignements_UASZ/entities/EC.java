package com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le code est obligatoire")
    private String code;
    
    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;
    
    @Min(value = 1, message = "Le crédit doit être au minimum 1")
    @Max(value = 30, message = "Le crédit doit être au maximum 30")
    private int credit;
    
    @Min(value = 0, message = "Les heures CM doivent être au minimum 0")
    @Max(value = 100, message = "Les heures CM doivent être au maximum 100")
    private int heureCm;
    
    @Min(value = 0, message = "Les heures TD doivent être au minimum 0")
    @Max(value = 100, message = "Les heures TD doivent être au maximum 100")
    private int heureTd;
    
    @Min(value = 0, message = "Les heures TP doivent être au minimum 0")
    @Max(value = 100, message = "Les heures TP doivent être au maximum 100")
    private int heureTp;
    
    @DecimalMin(value = "1", message = "Le coefficient doit être au minimum 1")
    @DecimalMax(value = "5", message = "Le coefficient doit être au maximum 5")
    private double coefficient = 1;
    
    @Min(value = 0, message = "Le TPE doit être au minimum 0")
    @Max(value = 100, message = "Le TPE doit être au maximum 100")
    private int tpe = 0;
    
    private boolean archive = false;
    private boolean actif = true;
    
    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
}
