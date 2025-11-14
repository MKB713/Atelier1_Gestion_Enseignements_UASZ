package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEnseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.MatriculeAlreadyExistsException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public Enseignant getEnseignantById(Long id) {
        return enseignantRepository.findById(id).orElse(null);
    }

    public void saveEnseignant(Enseignant enseignant) {
        // Vérifier l'unicité du matricule
        Optional<Enseignant> existingEnseignantByMatricule = enseignantRepository.findByMatricule(enseignant.getMatricule());

        if (existingEnseignantByMatricule.isPresent() && !existingEnseignantByMatricule.get().getId().equals(enseignant.getId())) {
            throw new MatriculeAlreadyExistsException("Le matricule " + enseignant.getMatricule() + " existe déjà pour un autre enseignant.");
        }

        if (enseignant.getId() != null) {
            // Mise à jour
            Enseignant originalEnseignant = enseignantRepository.findById(enseignant.getId())
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé pour la mise à jour avec l'id: " + enseignant.getId()));

            originalEnseignant.setMatricule(enseignant.getMatricule());
            originalEnseignant.setNom(enseignant.getNom());
            originalEnseignant.setPrenom(enseignant.getPrenom());
            originalEnseignant.setDateNaissance(enseignant.getDateNaissance());
            originalEnseignant.setLieuNaissance(enseignant.getLieuNaissance());
            originalEnseignant.setAdresse(enseignant.getAdresse());
            originalEnseignant.setEmail(enseignant.getEmail());
            originalEnseignant.setTelephone(enseignant.getTelephone());
            originalEnseignant.setGrade(enseignant.getGrade());
            originalEnseignant.setSpecialite(enseignant.getSpecialite());
            originalEnseignant.setStatut(enseignant.getStatut());
            originalEnseignant.setDateEmbauche(enseignant.getDateEmbauche());
            originalEnseignant.setEstActif(enseignant.isEstActif());
            originalEnseignant.setArchived(enseignant.isArchived());

            // Si réactivé, statut redevient ACTIF
            if (enseignant.isEstActif() && originalEnseignant.getStatutEnseignant() == StatutEnseignant.ARCHIVE) {
                originalEnseignant.setStatutEnseignant(StatutEnseignant.ACTIF);
                originalEnseignant.setArchived(false);
            }

            originalEnseignant.setDateModification(LocalDateTime.now());
            enseignantRepository.save(originalEnseignant);
        } else {
            // Nouvelle création
            enseignant.setDateCreation(LocalDateTime.now());
            enseignant.setDateModification(LocalDateTime.now());
            enseignant.setStatutEnseignant(StatutEnseignant.ACTIF);
            enseignant.setArchived(false);
            enseignantRepository.save(enseignant);
        }
    }

    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }

    public Enseignant archiverEnseignant(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id : " + id));

        enseignant.setStatutEnseignant(StatutEnseignant.ARCHIVE);
        enseignant.setArchived(true);
        enseignant.setDateModification(LocalDateTime.now());
        enseignantRepository.save(enseignant);

        return enseignant;
    }

    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findByArchivedFalse(); // enseignants non archivés
    }

    public List<Enseignant> getAllEnseignantsArchives() {
        return enseignantRepository.findByArchivedTrue(); // enseignants archivés
    }

    // ✅ Désarchiver un enseignant
    public void desarchiverEnseignant(Long id) {
        Enseignant e = enseignantRepository.findById(id).orElseThrow();
        e.setEstActif(true);
        e.setStatutEnseignant(StatutEnseignant.ACTIF);
        e.setArchived(false);
        e.setDateModification(LocalDateTime.now());
        enseignantRepository.save(e);
    }
}
