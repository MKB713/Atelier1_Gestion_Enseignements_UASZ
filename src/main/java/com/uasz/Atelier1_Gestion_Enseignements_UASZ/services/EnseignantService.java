package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Statut;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    public Enseignant getEnseignantById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id: " + id));
    }

    public void saveEnseignant(Enseignant enseignant) {
        if (enseignant.getId() == null) {
            enseignant.setDateCreation(LocalDateTime.now());
        }
        enseignant.setDateModification(LocalDateTime.now());
        enseignantRepository.save(enseignant);
    }

    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }
    // ➕ Ajouter à la fin de la classe EnseignantService

    public void changerStatutActif(Long id) {
        Enseignant enseignant = getEnseignantById(id);

        // Impossible de désactiver un enseignant archivé (statut VACATAIRE)
        if (!enseignant.isEstActif() && enseignant.getStatut() == Statut.VACATAIRE) {
            throw new RuntimeException("Impossible de désactiver un enseignant archivé !");
        }

        // Bascule du statut actif/inactif
        enseignant.setEstActif(!enseignant.isEstActif());
        enseignant.setDateModification(LocalDateTime.now());

        // Enregistre les changements
        enseignantRepository.save(enseignant);
    }

    public void activerEnseignant(Long id) {
        Enseignant enseignant = getEnseignantById(id);
        if (!enseignant.isEstActif()) {
            enseignant.setEstActif(true);
            enseignant.setDateModification(LocalDateTime.now());
            enseignantRepository.save(enseignant);
        }
    }

    public void desactiverEnseignant(Long id) {
        Enseignant enseignant = getEnseignantById(id);
        if (enseignant.isEstActif()) {
            // On empêche de désactiver un enseignant archivé
            if (enseignant.getStatut() == Statut.VACATAIRE) {
                throw new RuntimeException("Impossible de désactiver un enseignant archivé !");
            }
            enseignant.setEstActif(false);
            enseignant.setDateModification(LocalDateTime.now());
            enseignantRepository.save(enseignant);
        }
    }


}
