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
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id: " + id));
    }

    public void saveEnseignant(Enseignant enseignant) {
        // Vérifier l'unicité du matricule
        Optional<Enseignant> existingEnseignant = enseignantRepository.findByMatricule(enseignant.getMatricule());

        if (existingEnseignant.isPresent()) {
            // Si c'est une modification, vérifier que ce n'est pas le même enseignant
            if (enseignant.getId() == null || !existingEnseignant.get().getId().equals(enseignant.getId())) {
                throw new MatriculeAlreadyExistsException("Le matricule " + enseignant.getMatricule() + " existe déjà");
            }
        }

        if (enseignant.getId() == null) {
            enseignant.setDateCreation(LocalDateTime.now());
            enseignant.setStatutEnseignant(StatutEnseignant.ACTIF);
        }
        enseignant.setDateModification(LocalDateTime.now());
        enseignantRepository.save(enseignant);
    }

    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }



    public Enseignant archiverEnseignant(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id : " + id));

        enseignant.setStatutEnseignant(StatutEnseignant.ARCHIVE);
        enseignant.setDateModification(LocalDateTime.now());
        enseignantRepository.save(enseignant);

        return enseignant;
    }

    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findByStatutEnseignantNot(StatutEnseignant.ARCHIVE);
    }
    public List<Enseignant> getAllEnseignantsArchives() {
        return enseignantRepository.findByStatutEnseignant(StatutEnseignant.ARCHIVE);
    }
}
