package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEnseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.MatriculeAlreadyExistsException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<Enseignant> rechercherEnseignant(Long matricule) {
        if (matricule == null) {
            return Optional.empty();
        }
        return enseignantRepository.findByMatricule(matricule);
    }

    @Transactional
    public Enseignant updateEnseignant(Long id, EnseignantUpdateDTO updateDTO) {
        // Vérifier que l'enseignant existe
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id: " + id));

        // Valider les données modifiées
        validateUpdateData(updateDTO);

        // Mettre à jour uniquement les champs autorisés
        if (updateDTO.getGrade() != null && !updateDTO.getGrade().trim().isEmpty()) {
            enseignant.setGrade(updateDTO.getGrade());
        }

        if (updateDTO.getStatut() != null) {
            enseignant.setStatut(updateDTO.getStatut());
        }

        if (updateDTO.getEmail() != null && !updateDTO.getEmail().trim().isEmpty()) {
            // Cherche un enseignant avec cet email MAIS un ID différent de celui qu'on modifie
            enseignantRepository.findByEmailAndIdIsNot(updateDTO.getEmail(), id)
                    .ifPresent(existingEnseignant -> {
                        // Si un résultat est présent, l'email est utilisé par un AUTRE enseignant.
                        throw new IllegalArgumentException("Cet email est déjà utilisé par un autre enseignant.");
                    });
            enseignant.setEmail(updateDTO.getEmail());
        }

        if (updateDTO.getTelephone() != null && !updateDTO.getTelephone().trim().isEmpty()) {
            enseignant.setTelephone(updateDTO.getTelephone());
        }

        if (updateDTO.getAdresse() != null && !updateDTO.getAdresse().trim().isEmpty()) {
            enseignant.setAdresse(updateDTO.getAdresse());
        }

        // Enregistrer la date de modification
        enseignant.setDateModification(LocalDateTime.now());

        // Sauvegarder les modifications
        return enseignantRepository.save(enseignant);
    }

    private void validateUpdateData(EnseignantUpdateDTO updateDTO) {
        // Validation des champs
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().trim().isEmpty()) {
            if (!isValidEmail(updateDTO.getEmail())) {
                throw new IllegalArgumentException("Format d'email invalide");
            }
        }

        if (updateDTO.getTelephone() != null && !updateDTO.getTelephone().trim().isEmpty()) {
            if (!isValidTelephone(updateDTO.getTelephone())) {
                throw new IllegalArgumentException("Format de téléphone invalide");
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidTelephone(String telephone) {
        return telephone != null && telephone.matches("^[+]?[0-9\\s\\-\\(\\)]{7,20}$");
    }

    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }
}
