package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            // Vérifier que l'email n'est pas déjà utilisé par un autre enseignant
            enseignantRepository.findByEmail(updateDTO.getEmail())
                    .ifPresent(existingEnseignant -> {
                        if (!existingEnseignant.getId().equals(id)) {
                            throw new RuntimeException("Cet email est déjà utilisé par un autre enseignant");
                        }
                    });
            enseignant.setEmail(updateDTO.getEmail());
        }

        if (updateDTO.getTelephone() != null && !updateDTO.getTelephone().trim().isEmpty()) {
            enseignant.setTelephone(updateDTO.getTelephone());
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
        return telephone != null && telephone.matches("^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$");
    }

    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }
}
