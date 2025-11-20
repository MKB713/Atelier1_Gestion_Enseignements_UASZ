package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Filiere;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Formation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Niveau;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.StatutFormation;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FormationRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.FiliereRepository;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FormationService {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private NiveauRepository niveauRepository;

    public List<Formation> getAllFormations() {
        return formationRepository.findAll();
    }

    public List<Formation> getActiveFormations() {
        return formationRepository.findByStatutFormation(StatutFormation.ACTIVE);
    }

    public Optional<Formation> getFormationById(Long id) {
        return formationRepository.findById(id);
    }

    @Transactional
    public Formation createFormation(Formation formation) {
        // Vérification unicité code
        if (formation.getCode() != null) {
            formationRepository.findByCode(formation.getCode()).ifPresent(f -> {
                throw new IllegalArgumentException("Un code de formation identique existe déjà.");
            });
        }

        // Vérification unicité libelle
        if (formation.getLibelle() != null) {
            formationRepository.findByLibelle(formation.getLibelle()).ifPresent(f -> {
                throw new IllegalArgumentException("Un libellé de formation identique existe déjà.");
            });
        }

        // Charger les entités complètes depuis la base de données
        if (formation.getFiliere() != null && formation.getFiliere().getId() != null) {
            Filiere filiere = filiereRepository.findById(formation.getFiliere().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Filière non trouvée"));
            formation.setFiliere(filiere);
        }

        if (formation.getNiveau() != null && formation.getNiveau().getId() != null) {
            Niveau niveau = niveauRepository.findById(formation.getNiveau().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Niveau non trouvé"));
            formation.setNiveau(niveau);
        }

        if (formation.getDateCreation() == null) {
            formation.setDateCreation(new Date());
        }
        formation.setStatutFormation(StatutFormation.ACTIVE);
        return formationRepository.save(formation);
    }

    @Transactional
    public Formation updateFormation(Long id, Formation updated) {
        Formation found = formationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Formation non trouvée avec l'id: " + id));

        // Vérifier unicité libelle/code si modifiés
        if (updated.getCode() != null && !updated.getCode().equals(found.getCode())) {
            formationRepository.findByCode(updated.getCode()).ifPresent(f -> {
                throw new IllegalArgumentException("Un autre formation utilise déjà ce code.");
            });
            found.setCode(updated.getCode());
        }

        if (updated.getLibelle() != null && !updated.getLibelle().equals(found.getLibelle())) {
            formationRepository.findByLibelle(updated.getLibelle()).ifPresent(f -> {
                throw new IllegalArgumentException("Un autre formation utilise déjà ce libellé.");
            });
            found.setLibelle(updated.getLibelle());
        }

        if (updated.getDescription() != null) {
            found.setDescription(updated.getDescription());
        }

        if (updated.getFiliere() != null && updated.getFiliere().getId() != null) {
            // facultatif : vérification existence filière (si besoin)
            found.setFiliere(filiereRepository.findById(updated.getFiliere().getId()).orElse(null));
        }

        if (updated.getNiveau() != null && updated.getNiveau().getId() != null) {
            found.setNiveau(niveauRepository.findById(updated.getNiveau().getId()).orElse(null));
        }

        return formationRepository.save(found);
    }

    @Transactional
    public Formation archiveFormation(Long id) {
        Formation found = formationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Formation non trouvée avec l'id: " + id));
        found.setStatutFormation(StatutFormation.ARCHIVE);
        return formationRepository.save(found);
    }

    public List<Formation> searchByLibelle(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        return formationRepository.findByLibelleContainingIgnoreCase(query.trim());
    }



    @Transactional
    public Formation activerFormation(Long id) {
        Formation found = formationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Formation non trouvée avec l'id: " + id));
        found.setStatutFormation(StatutFormation.ACTIVE);
        return formationRepository.save(found);
    }
}
