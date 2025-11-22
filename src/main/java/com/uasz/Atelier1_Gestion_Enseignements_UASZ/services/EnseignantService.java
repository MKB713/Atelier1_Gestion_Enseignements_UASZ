package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEnseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.exceptions.MatriculeAlreadyExistsException;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
            // Initialisation de estActif à true par défaut lors de la création
            enseignant.setEstActif(true);
        }
        String password = generatePassword();
        enseignant.setPassword(bCryptPasswordEncoder.encode(password));
        String subject = " Création de votre compte UASZ ";
        String text = "Bonjour " + enseignant.getNom() + " " + enseignant.getPrenom() + " !\n\n"
                + "Votre compte a été créé avec succès.\n"
                + "Voici vos identifiants de connexion :\n"
                + "Email : " + enseignant.getEmail() + "\n"
                + "Mot de passe : " + password + "\n\n"
                + "Merci de conserver ces informations en lieu sûr.\n"
                + "Cordialement,\n"
                + "L'équipe UASZ";

        mailService.sendMail(enseignant.getEmail(),subject,text);
        enseignant.setDateModification(LocalDateTime.now());
        enseignantRepository.save(enseignant);
    }
    public String generatePassword() {
        String password = UUID.randomUUID().toString().substring(0, 6);
        return password;
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
            List<Enseignant> existingEnseignants = enseignantRepository.findByEmailAndIdIsNot(updateDTO.getEmail(), id);

            // Si la liste contient AU MOINS un enseignant, l'email est déjà pris.
            if (!existingEnseignants.isEmpty()) {
                throw new IllegalArgumentException("Cet email est déjà utilisé par un autre enseignant.");
            }
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


    // NOUVELLES MÉTHODES POUR L'ACTIVATION/DÉSACTIVATION

    /**
     * Active l'enseignant (estActif = true).
     * @param id L'identifiant de l'enseignant.
     * @return L'enseignant mis à jour.
     */
    @Transactional
    public Enseignant activerEnseignant(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id: " + id));

        enseignant.setEstActif(true);
        enseignant.setDateModification(LocalDateTime.now());
        return enseignantRepository.save(enseignant);
    }

    /**
     * Désactive l'enseignant (estActif = false).
     * @param id L'identifiant de l'enseignant.
     * @return L'enseignant mis à jour.
     */
    @Transactional
    public Enseignant desactiverEnseignant(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id: " + id));

        enseignant.setEstActif(false);
        enseignant.setDateModification(LocalDateTime.now());
        return enseignantRepository.save(enseignant);
    }
}