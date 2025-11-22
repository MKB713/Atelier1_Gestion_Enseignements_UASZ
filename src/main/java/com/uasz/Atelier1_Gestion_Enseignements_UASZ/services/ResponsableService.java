package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.ResponsableDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Responsable;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.TypeResponsable;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ResponsableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ResponsableService {

    @Autowired
    private ResponsableRepository responsableRepository;

    @Autowired
    private FormationService formationService;

    @Autowired
    private EnseignantService enseignantService;

    /**
     * Récupère tous les responsables
     */
    public List<Responsable> getAllResponsables() {
        return responsableRepository.findAll();
    }

    /**
     * Récupère un responsable par son ID
     */
    public Responsable getResponsableById(Long id) {
        return responsableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé avec l'ID : " + id));
    }

    /**
     * Crée un nouveau responsable
     */
    public Responsable creerResponsable(ResponsableDTO dto) {
        // Vérification de l'unicité de l'email
        if (responsableRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Un responsable avec cet email existe déjà : " + dto.getEmail());
        }

        Responsable responsable = new Responsable();
        responsable.setNom(dto.getNom());
        responsable.setPrenom(dto.getPrenom());
        responsable.setEmail(dto.getEmail());
        responsable.setTelephone(dto.getTelephone());
        responsable.setType(dto.getType());

        // Associer la formation si fournie
        if (dto.getFormationId() != null) {
            Formation formation = formationService.getFormationById(dto.getFormationId());
            responsable.setFormation(formation);
        }

        // Associer l'enseignant si fourni
        if (dto.getEnseignantId() != null) {
            Enseignant enseignant = enseignantService.getEnseignantById(dto.getEnseignantId());
            responsable.setEnseignant(enseignant);
        }

        responsable.setDateDebutFonction(dto.getDateDebutFonction() != null ? dto.getDateDebutFonction() : LocalDate.now());
        responsable.setDateFinFonction(dto.getDateFinFonction());
        responsable.setActif(dto.getActif() != null ? dto.getActif() : true);
        responsable.setRemarques(dto.getRemarques());

        return responsableRepository.save(responsable);
    }

    /**
     * Met à jour un responsable existant
     */
    public Responsable modifierResponsable(Long id, ResponsableDTO dto) {
        Responsable responsable = getResponsableById(id);

        // Vérification de l'email si modifié
        if (!responsable.getEmail().equals(dto.getEmail())) {
            if (responsableRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Un responsable avec cet email existe déjà : " + dto.getEmail());
            }
            responsable.setEmail(dto.getEmail());
        }

        responsable.setNom(dto.getNom());
        responsable.setPrenom(dto.getPrenom());
        responsable.setTelephone(dto.getTelephone());
        responsable.setType(dto.getType());

        // Mise à jour de la formation
        if (dto.getFormationId() != null) {
            Formation formation = formationService.getFormationById(dto.getFormationId());
            responsable.setFormation(formation);
        } else {
            responsable.setFormation(null);
        }

        // Mise à jour de l'enseignant
        if (dto.getEnseignantId() != null) {
            Enseignant enseignant = enseignantService.getEnseignantById(dto.getEnseignantId());
            responsable.setEnseignant(enseignant);
        } else {
            responsable.setEnseignant(null);
        }

        if (dto.getDateDebutFonction() != null) {
            responsable.setDateDebutFonction(dto.getDateDebutFonction());
        }
        responsable.setDateFinFonction(dto.getDateFinFonction());

        if (dto.getActif() != null) {
            responsable.setActif(dto.getActif());
        }
        responsable.setRemarques(dto.getRemarques());

        return responsableRepository.save(responsable);
    }

    /**
     * Désactive un responsable
     */
    public void desactiverResponsable(Long id) {
        Responsable responsable = getResponsableById(id);
        responsable.setActif(false);
        responsable.setDateFinFonction(LocalDate.now());
        responsableRepository.save(responsable);
    }

    /**
     * Réactive un responsable
     */
    public void reactiverResponsable(Long id) {
        Responsable responsable = getResponsableById(id);
        responsable.setActif(true);
        responsable.setDateFinFonction(null);
        responsableRepository.save(responsable);
    }

    /**
     * Récupère les responsables actifs
     */
    public List<Responsable> getResponsablesActifs() {
        return responsableRepository.findByActifTrue();
    }

    /**
     * Récupère les responsables par type
     */
    public List<Responsable> getResponsablesParType(TypeResponsable type) {
        return responsableRepository.findByType(type);
    }

    /**
     * Récupère les responsables de licence
     */
    public List<Responsable> getResponsablesLicence() {
        return responsableRepository.findByTypeAndActifTrue(TypeResponsable.LICENCE);
    }

    /**
     * Récupère les responsables de master
     */
    public List<Responsable> getResponsablesMaster() {
        return responsableRepository.findByTypeAndActifTrue(TypeResponsable.MASTER);
    }

    /**
     * Recherche des responsables
     */
    public List<Responsable> rechercherResponsables(String term) {
        return responsableRepository.rechercherResponsables(term);
    }

    /**
     * Récupère le responsable actif d'une formation
     */
    public Responsable getResponsableActifFormation(Long formationId) {
        return responsableRepository.findResponsableActifByFormation(formationId)
                .orElse(null);
    }

    /**
     * Supprime un responsable
     */
    public void supprimerResponsable(Long id) {
        Responsable responsable = getResponsableById(id);
        responsableRepository.delete(responsable);
    }
}
