package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.InscriptionDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Classe;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Etudiant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Inscription;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutInscription;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class InscriptionService {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private ClasseService classeService;

    @Autowired
    private FormationService formationService;

    /**
     * Récupère toutes les inscriptions
     */
    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    /**
     * Récupère une inscription par son ID
     */
    public Inscription getInscriptionById(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée avec l'ID : " + id));
    }

    /**
     * Crée une nouvelle inscription
     */
    public Inscription creerInscription(InscriptionDTO dto) {
        // Récupération de l'étudiant
        Etudiant etudiant = etudiantService.getEtudiantById(dto.getEtudiantId());

        // Vérifier si l'étudiant n'est pas déjà inscrit dans cette classe pour cette année
        if (dto.getClasseId() != null) {
            inscriptionRepository.findByEtudiantAndClasseAndAnnee(
                    dto.getEtudiantId(),
                    dto.getClasseId(),
                    dto.getAnneeAcademique()
            ).ifPresent(i -> {
                throw new IllegalArgumentException(
                        "L'étudiant est déjà inscrit dans cette classe pour l'année " + dto.getAnneeAcademique()
                );
            });
        }

        Inscription inscription = new Inscription();
        inscription.setEtudiant(etudiant);

        // Associer la classe si fournie
        if (dto.getClasseId() != null) {
            Classe classe = classeService.getClasseById(dto.getClasseId());
            inscription.setClasse(classe);

            // Si la classe a une formation, l'associer automatiquement
            if (classe.getFiliere() != null) {
                // On peut déterminer la formation à partir de la classe
                // Pour l'instant, on utilise directement ce qui est fourni
            }
        }

        // Associer la formation si fournie
        if (dto.getFormationId() != null) {
            Formation formation = formationService.getFormationById(dto.getFormationId());
            inscription.setFormation(formation);
        }

        inscription.setAnneeAcademique(dto.getAnneeAcademique());
        inscription.setAnneeFormation(dto.getAnneeFormation());
        inscription.setDateInscription(dto.getDateInscription() != null ? dto.getDateInscription() : LocalDate.now());
        inscription.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutInscription.EN_COURS);
        inscription.setEstRedoublant(dto.getEstRedoublant() != null ? dto.getEstRedoublant() : false);
        inscription.setRemarques(dto.getRemarques());

        return inscriptionRepository.save(inscription);
    }

    /**
     * Met à jour une inscription existante
     */
    public Inscription modifierInscription(Long id, InscriptionDTO dto) {
        Inscription inscription = getInscriptionById(id);

        // Mise à jour de la classe
        if (dto.getClasseId() != null) {
            Classe classe = classeService.getClasseById(dto.getClasseId());
            inscription.setClasse(classe);
        }

        // Mise à jour de la formation
        if (dto.getFormationId() != null) {
            Formation formation = formationService.getFormationById(dto.getFormationId());
            inscription.setFormation(formation);
        }

        inscription.setAnneeAcademique(dto.getAnneeAcademique());
        inscription.setAnneeFormation(dto.getAnneeFormation());

        if (dto.getStatut() != null) {
            inscription.setStatut(dto.getStatut());
        }

        if (dto.getEstRedoublant() != null) {
            inscription.setEstRedoublant(dto.getEstRedoublant());
        }

        inscription.setRemarques(dto.getRemarques());

        return inscriptionRepository.save(inscription);
    }

    /**
     * Valide une inscription
     */
    public void validerInscription(Long id) {
        Inscription inscription = getInscriptionById(id);
        inscription.setStatut(StatutInscription.VALIDEE);
        inscriptionRepository.save(inscription);
    }

    /**
     * Marque une inscription comme redoublement
     */
    public void marquerCommeRedoublement(Long id) {
        Inscription inscription = getInscriptionById(id);
        inscription.setStatut(StatutInscription.REDOUBLEMENT);
        inscription.setEstRedoublant(true);
        inscriptionRepository.save(inscription);
    }

    /**
     * Annule une inscription
     */
    public void annulerInscription(Long id) {
        Inscription inscription = getInscriptionById(id);
        inscription.setStatut(StatutInscription.ABANDONNEE);
        inscriptionRepository.save(inscription);
    }

    /**
     * Récupère les inscriptions d'un étudiant
     */
    public List<Inscription> getInscriptionsEtudiant(Long etudiantId) {
        Etudiant etudiant = etudiantService.getEtudiantById(etudiantId);
        return inscriptionRepository.findByEtudiant(etudiant);
    }

    /**
     * Récupère les inscriptions actives d'un étudiant
     */
    public List<Inscription> getInscriptionsActivesEtudiant(Long etudiantId) {
        return inscriptionRepository.findInscriptionsActivesEtudiant(etudiantId);
    }

    /**
     * Récupère les inscriptions d'une classe
     */
    public List<Inscription> getInscriptionsClasse(Long classeId) {
        Classe classe = classeService.getClasseById(classeId);
        return inscriptionRepository.findByClasse(classe);
    }

    /**
     * Récupère les inscriptions d'une formation
     */
    public List<Inscription> getInscriptionsFormation(Long formationId) {
        Formation formation = formationService.getFormationById(formationId);
        return inscriptionRepository.findByFormation(formation);
    }

    /**
     * Récupère les inscriptions par année académique
     */
    public List<Inscription> getInscriptionsParAnnee(String anneeAcademique) {
        return inscriptionRepository.findByAnneeAcademique(anneeAcademique);
    }

    /**
     * Compte le nombre d'inscriptions dans une classe
     */
    public long compterInscriptionsClasse(Long classeId) {
        Classe classe = classeService.getClasseById(classeId);
        return inscriptionRepository.countByClasse(classe);
    }

    /**
     * Récupère les redoublants d'une classe
     */
    public List<Inscription> getRedoublantsClasse(Long classeId) {
        return inscriptionRepository.findRedoublantsParClasse(classeId);
    }

    /**
     * Supprime une inscription
     */
    public void supprimerInscription(Long id) {
        Inscription inscription = getInscriptionById(id);
        inscriptionRepository.delete(inscription);
    }
}
