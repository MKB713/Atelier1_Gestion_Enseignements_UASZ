package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Enseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutEnseignant;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    // ----------------------------------------------------------------------
    // --- LOGIQUE METIER : VALIDATIONS & UTILITAIRES ---
    // ----------------------------------------------------------------------

    /**
     * Nettoie une chaîne (enlève accents, espaces, met en minuscule)
     */
    private String cleanString(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase()
                .trim()
                .replace(" ", "");
    }

    /**
     * Génère un email institutionnel unique : prenom.nom@univ-zig.sn
     */
    private String generateUniqueEmail(String prenom, String nom) {
        String cleanPrenom = cleanString(prenom);
        String cleanNom = cleanString(nom);
        String domain = "@univ-zig.sn";

        String baseEmail = cleanPrenom + "." + cleanNom;
        String candidateEmail = baseEmail + domain;

        int counter = 1;
        while (enseignantRepository.findByEmail(candidateEmail).isPresent()) {
            candidateEmail = baseEmail + counter + domain;
            counter++;
        }
        return candidateEmail;
    }

    /**
     * Vérifie que la date n'est pas dans le futur
     */
    private void validateDateEmbauche(LocalDate dateEmbauche) {
        if (dateEmbauche != null && dateEmbauche.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date d'embauche ne peut pas être une date future (" + dateEmbauche + ").");
        }
    }

    /**
     * Vérifie que l'enseignant a au moins 25 ans
     */
    private void validateAge(LocalDate dateNaissance) {
        if (dateNaissance != null) {
            LocalDate dateMinimum = LocalDate.now().minusYears(25);
            // Si la date de naissance est APRÈS la date limite (donc plus jeune que 25 ans)
            if (dateNaissance.isAfter(dateMinimum)) {
                throw new IllegalArgumentException("L'enseignant doit être âgé d'au moins 25 ans.");
            }
        }
    }

    private void validateEmailFormat(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
    }

    private void validateTelephoneFormat(String telephone) {
        if (telephone == null || !telephone.matches("^\\+?[0-9. ()-]{7,25}$")) {
            throw new IllegalArgumentException("Format de téléphone invalide");
        }
    }

    // ----------------------------------------------------------------------
    // --- GENERATION MATRICULE ---
    // ----------------------------------------------------------------------
    private Long generateNextMatricule() {
        int currentYear = Year.now().getValue();
        final long YEAR_BASE = (long) currentYear * 100000L;

        List<Enseignant> allEnseignants = enseignantRepository.findAll();

        Optional<Long> maxMatriculeThisYearOpt = allEnseignants.stream()
                .filter(e -> e.getMatricule() != null && e.getMatricule() >= YEAR_BASE && e.getMatricule() < YEAR_BASE + 100000)
                .map(Enseignant::getMatricule)
                .max(Comparator.naturalOrder());

        long nextRank = maxMatriculeThisYearOpt.map(maxMatricule -> (maxMatricule % 100000L) + 1L).orElse(1L);

        if (nextRank >= 100000L) {
            throw new RuntimeException("Erreur de séquence : Limite atteinte pour l'année " + currentYear);
        }

        return YEAR_BASE + nextRank;
    }

    // ----------------------------------------------------------------------
    // --- CRUD OPERATIONS ---
    // ----------------------------------------------------------------------

    public Enseignant getEnseignantById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé avec l'id: " + id));
    }

    @Transactional
    public void saveEnseignant(Enseignant enseignant) {
        // 1. Validations
        validateDateEmbauche(enseignant.getDateEmbauche());
        validateAge(enseignant.getDateNaissance()); // Ajout de la validation d'âge

        // 2. Génération email
        String generatedEmail = generateUniqueEmail(enseignant.getPrenom(), enseignant.getNom());
        enseignant.setEmail(generatedEmail);

        // 3. Initialisation
        if (enseignant.getId() == null) {
            enseignant.setMatricule(generateNextMatricule());
            enseignant.setDateCreation(LocalDateTime.now());
            enseignant.setStatutEnseignant(StatutEnseignant.ACTIF);
            enseignant.setEstActif(true);
        }

        enseignant.setDateModification(LocalDateTime.now());
        enseignantRepository.save(enseignant);
    }

    @Transactional
    public Enseignant updateEnseignant(Long id, com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.EnseignantUpdateDTO updateDTO) {
        Enseignant enseignant = getEnseignantById(id);

        // 1. Validations (apply only if DTO fields are present)
        if (updateDTO.getDateEmbauche() != null) {
            validateDateEmbauche(updateDTO.getDateEmbauche());
        }
        if (updateDTO.getEmail() != null) {
            validateEmailFormat(updateDTO.getEmail());
        }
        if (updateDTO.getTelephone() != null) {
            validateTelephoneFormat(updateDTO.getTelephone());
        }

        // Note: DateNaissance and LieuNaissance are not in EnseignantUpdateDTO, so no validation/update here.

        // 2. Mise à jour des champs
        if (updateDTO.getNom() != null) {
            enseignant.setNom(updateDTO.getNom());
        }
        if (updateDTO.getPrenom() != null) {
            enseignant.setPrenom(updateDTO.getPrenom());
        }
        if (updateDTO.getSpecialite() != null) {
            enseignant.setSpecialite(updateDTO.getSpecialite());
        }
        if (updateDTO.getDateEmbauche() != null) {
            enseignant.setDateEmbauche(updateDTO.getDateEmbauche());
        }
        if (updateDTO.getGrade() != null) {
            enseignant.setGrade(updateDTO.getGrade());
        }
        if (updateDTO.getStatut() != null) {
            enseignant.setStatut(updateDTO.getStatut());
        }
        if (updateDTO.getTelephone() != null) {
            enseignant.setTelephone(updateDTO.getTelephone());
        }
        if (updateDTO.getAdresse() != null) {
            enseignant.setAdresse(updateDTO.getAdresse());
        }
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(enseignant.getEmail())) {
            // Validate if new email is already used by another enseignant
            Optional<Enseignant> existingEnseignantWithEmail = enseignantRepository.findByEmail(updateDTO.getEmail());
            if (existingEnseignantWithEmail.isPresent() && !existingEnseignantWithEmail.get().getId().equals(enseignant.getId())) {
                throw new IllegalArgumentException("Cet email est déjà utilisé par un autre enseignant.");
            }
            enseignant.setEmail(updateDTO.getEmail());
        }

        enseignant.setDateModification(LocalDateTime.now());

        return enseignantRepository.save(enseignant);
    }

    // ----------------------------------------------------------------------
    // --- LISTE ET RECHERCHE ---
    // ----------------------------------------------------------------------

    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findByStatutEnseignantNot(StatutEnseignant.ARCHIVE);
    }

    public List<Enseignant> getAllEnseignantsArchives() {
        return enseignantRepository.findByStatutEnseignant(StatutEnseignant.ARCHIVE);
    }

    // ----------------------------------------------------------------------
    // --- GESTION DU STATUT ---
    // ----------------------------------------------------------------------

    @Transactional
    public Enseignant archiverEnseignant(Long id) {
        Enseignant enseignant = getEnseignantById(id);
        enseignant.setStatutEnseignant(StatutEnseignant.ARCHIVE);
        enseignant.setEstActif(false);
        enseignant.setDateModification(LocalDateTime.now());
        return enseignantRepository.save(enseignant);
    }

    @Transactional
    public Enseignant desarchiverEnseignant(Long id) {
        Enseignant enseignant = getEnseignantById(id);
        enseignant.setStatutEnseignant(StatutEnseignant.ACTIF);
        enseignant.setEstActif(true);
        enseignant.setDateModification(LocalDateTime.now());
        return enseignantRepository.save(enseignant);
    }

    @Transactional
    public Enseignant activerEnseignant(Long id) {
        Enseignant enseignant = getEnseignantById(id);
        if (enseignant.getStatutEnseignant() == StatutEnseignant.ARCHIVE) {
            throw new RuntimeException("Impossible d'activer un enseignant archivé.");
        }
        enseignant.setEstActif(true);
        enseignant.setDateModification(LocalDateTime.now());
        return enseignantRepository.save(enseignant);
    }

    @Transactional
    public Enseignant desactiverEnseignant(Long id) {
        Enseignant enseignant = getEnseignantById(id);
        if (enseignant.getStatutEnseignant() == StatutEnseignant.ARCHIVE) {
            throw new RuntimeException("Impossible de désactiver un enseignant archivé.");
        }
        enseignant.setEstActif(false);
        enseignant.setDateModification(LocalDateTime.now());
        return enseignantRepository.save(enseignant);
    }
}