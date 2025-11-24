package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Niveau;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FiliereRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FormationRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormationService {

    @Autowired
    private FormationRepository formationRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private NiveauRepository niveauRepository;

    // --- LECTURE ---
    public List<Formation> getAllFormations() {
        // Retourne tout ce qui n'est pas archivé
        return formationRepository.findAll().stream()
                .filter(f -> !f.isArchive())
                .collect(Collectors.toList());
    }

    public List<Formation> getArchivedFormations() {
        return formationRepository.findAll().stream()
                .filter(Formation::isArchive)
                .collect(Collectors.toList());
    }

    public Formation getFormationById(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Formation introuvable : " + id));
    }

    // --- ECRITURE ---
    @Transactional
    public Formation save(Formation formation) {
        if (formation.getId() == null) return createFormation(formation);
        else return updateFormation(formation.getId(), formation);
    }

    @Transactional
    public Formation createFormation(Formation formation) {
        // Vérifications d'unicité (Code/Libellé)...
        // (Je raccourcis ici, gardez vos vérifications existantes)

        attachRelations(formation);
        if (formation.getDateCreation() == null) formation.setDateCreation(new Date());

        // Initialisation "Ancienne Méthode"
        formation.setActive(true);
        formation.setArchive(false);

        return formationRepository.save(formation);
    }

    @Transactional
    public Formation updateFormation(Long id, Formation updated) {
        Formation found = getFormationById(id);
        // Mise à jour des champs classiques...
        found.setCode(updated.getCode());
        found.setLibelle(updated.getLibelle());
        found.setDescription(updated.getDescription());
        if(updated.getFiliere() != null) found.setFiliere(updated.getFiliere());
        if(updated.getNiveau() != null) found.setNiveau(updated.getNiveau());

        attachRelations(found);
        return formationRepository.save(found);
    }

    private void attachRelations(Formation formation) {
        if (formation.getFiliere() != null && formation.getFiliere().getId() != null) {
            formation.setFiliere(filiereRepository.findById(formation.getFiliere().getId()).orElse(null));
        }
        if (formation.getNiveau() != null && formation.getNiveau().getId() != null) {
            formation.setNiveau(niveauRepository.findById(formation.getNiveau().getId()).orElse(null));
        }
    }

    // --- ACTIONS (BOOLÉENS) ---

    @Transactional
    public void archiveFormation(Long id) {
        Formation f = getFormationById(id);
        f.setArchive(true);
        f.setActive(false); // On désactive en archivant
        formationRepository.save(f);
    }

    @Transactional
    public void activerFormation(Long id) {
        Formation f = getFormationById(id);
        f.setActive(true); // Vrai booléen
        f.setArchive(false); // Si on restaure depuis les archives
        formationRepository.save(f);
    }

    @Transactional
    public void desactiverFormation(Long id) {
        Formation f = getFormationById(id);
        f.setActive(false); // Vrai booléen
        formationRepository.save(f);
    }
}