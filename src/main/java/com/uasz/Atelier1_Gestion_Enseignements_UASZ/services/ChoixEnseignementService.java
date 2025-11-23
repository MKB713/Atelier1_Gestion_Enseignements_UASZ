package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.ChoixEnseignementDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutChoix;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoixEnseignementService {

    @Autowired
    private ChoixEnseignementRepository choixRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private UERepository ueRepository;

    @Autowired
    private ECRepository ecRepository;

    //  Ajouter un choix d'enseignement
    @Transactional
    public ChoixEnseignement creerChoix(ChoixEnseignementDTO dto) {
        // Vérifier que l'enseignant existe
        Enseignant enseignant = enseignantRepository.findById(dto.getEnseignantId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Enseignant non trouvé avec l'id: " + dto.getEnseignantId()));

        // Vérifier que l'enseignant n'a pas déjà fait un choix pour ce semestre
        choixRepository.findByEnseignantIdAndSemestreAndStatutNot(
                        dto.getEnseignantId(), dto.getSemestre(), StatutChoix.REJETE)
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "Vous avez déjà soumis un choix pour le semestre " + dto.getSemestre() +
                                    ". Veuillez le modifier ou attendre qu'il soit traité.");
                });

        // Vérifier UE et EC
        UE ue = ueRepository.findById(dto.getUeId())
                .orElseThrow(() -> new EntityNotFoundException("UE non trouvée"));
        EC ec = ecRepository.findById(dto.getEcId())
                .orElseThrow(() -> new EntityNotFoundException("EC non trouvé"));

        // Créer le choix
        ChoixEnseignement choix = new ChoixEnseignement();
        choix.setEnseignant(enseignant);
        choix.setUe(ue);
        choix.setEc(ec);
        choix.setSemestre(dto.getSemestre());
        choix.setVolumeHoraire(dto.getVolumeHoraire());
        choix.setStatut(StatutChoix.EN_ATTENTE);
        choix.setDateCreation(LocalDateTime.now());

        return choixRepository.save(choix);
    }

    // Modifier un choix d'enseignement
    @Transactional
    public ChoixEnseignement modifierChoix(Long id, ChoixEnseignementDTO dto) {
        ChoixEnseignement choix = choixRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Choix non trouvé avec l'id: " + id));

        // Vérifier que le statut est EN_ATTENTE
        if (choix.getStatut() != StatutChoix.EN_ATTENTE) {
            throw new IllegalStateException(
                    "Impossible de modifier ce choix car il a déjà été " +
                            choix.getStatut().getDisplayValue().toLowerCase());
        }

        // Mettre à jour les champs
        if (dto.getUeId() != null) {
            UE ue = ueRepository.findById(dto.getUeId())
                    .orElseThrow(() -> new EntityNotFoundException("UE non trouvée"));
            choix.setUe(ue);
        }
        if (dto.getEcId() != null) {
            EC ec = ecRepository.findById(dto.getEcId())
                    .orElseThrow(() -> new EntityNotFoundException("EC non trouvé"));
            choix.setEc(ec);
        }
        if (dto.getSemestre() != null) {
            choix.setSemestre(dto.getSemestre());
        }
        if (dto.getVolumeHoraire() != null) {
            choix.setVolumeHoraire(dto.getVolumeHoraire());
        }

        choix.setDateModification(LocalDateTime.now());

        // Note: La notification au chef de département se fera via le contrôleur
        return choixRepository.save(choix);
    }

    //  Supprimer un choix d'enseignement
    @Transactional
    public void supprimerChoix(Long id) {
        ChoixEnseignement choix = choixRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Choix non trouvé avec l'id: " + id));

        // Vérifier que le statut est EN_ATTENTE
        if (choix.getStatut() != StatutChoix.EN_ATTENTE) {
            throw new IllegalStateException(
                    "Impossible de supprimer ce choix car il a déjà été " +
                            choix.getStatut().getDisplayValue().toLowerCase());
        }

        choixRepository.delete(choix);
    }

    // Rechercher les choix par matricule
    public List<ChoixEnseignementDTO> rechercherParMatricule(Long matricule) {
        List<ChoixEnseignement> choix = choixRepository.findByEnseignantMatricule(matricule);
        return choix.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    //  Méthodes utilitaires

    public List<ChoixEnseignement> getAllChoix() {
        return choixRepository.findAll();
    }

    public List<ChoixEnseignement> getChoixEnAttente() {
        return choixRepository.findByStatutOrderByDateCreationDesc(StatutChoix.EN_ATTENTE);
    }

    public List<ChoixEnseignement> getChoixByEnseignant(Long enseignantId) {
        return choixRepository.findByEnseignantId(enseignantId);
    }

    public ChoixEnseignement getChoixById(Long id) {
        return choixRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Choix non trouvé avec l'id: " + id));
    }

    // Validation par le chef de département
    @Transactional
    public ChoixEnseignement validerChoix(Long id, String commentaire) {
        ChoixEnseignement choix = getChoixById(id);
        choix.setStatut(StatutChoix.VALIDE);
        choix.setCommentaire(commentaire);
        choix.setDateModification(LocalDateTime.now());
        return choixRepository.save(choix);
    }

    // Rejet par le chef de département
    @Transactional
    public ChoixEnseignement rejeterChoix(Long id, String commentaire) {
        ChoixEnseignement choix = getChoixById(id);
        choix.setStatut(StatutChoix.REJETE);
        choix.setCommentaire(commentaire);
        choix.setDateModification(LocalDateTime.now());
        return choixRepository.save(choix);
    }

    // Conversion Entity -> DTO
    public ChoixEnseignementDTO convertToDTO(ChoixEnseignement choix) {
        ChoixEnseignementDTO dto = new ChoixEnseignementDTO();
        dto.setId(choix.getId());
        dto.setEnseignantId(choix.getEnseignant().getId());
        dto.setEnseignantNom(choix.getEnseignant().getPrenom() + " " + choix.getEnseignant().getNom());
        dto.setEnseignantMatricule(String.valueOf(choix.getEnseignant().getMatricule()));
        dto.setUeId(choix.getUe().getId());
        dto.setUeLibelle(choix.getUe().getLibelle());
        dto.setEcId(choix.getEc().getId());
        dto.setEcLibelle(choix.getEc().getLibelle());
        dto.setSemestre(choix.getSemestre());
        dto.setVolumeHoraire(choix.getVolumeHoraire());
        dto.setStatut(choix.getStatut());
        dto.setDateCreation(choix.getDateCreation());
        dto.setCommentaire(choix.getCommentaire());
        return dto;
    }
}