package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Maquette;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.MaquetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MaquetteService {

    @Autowired
    private MaquetteRepository maquetteRepository;

    @Autowired
    private FormationService formationService;

    public List<Maquette> getAllMaquettes() {
        return maquetteRepository.findAll();
    }

    public Optional<Maquette> getMaquetteById(Long id) {
        return maquetteRepository.findById(id);
    }

    public List<Maquette> getMaquettesByFormation(Long formationId) {
        return maquetteRepository.findByFormationId(formationId);
    }

    public List<Maquette> getActiveMaquettes() {
        return maquetteRepository.findByActiveTrue();
    }

    public List<Maquette> searchByLibelle(String libelle) {
        return maquetteRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    @Transactional
    public Maquette saveMaquette(Maquette maquette) {
        validateMaquette(maquette);

        // Vérifier qu'il n'y a pas déjà une maquette active pour la même formation et année
        if (maquette.isActive()) {
            Optional<Maquette> existingActive = maquetteRepository.findByFormationIdAndAnneeAcademique(
                    maquette.getFormation().getId(), maquette.getAnneeAcademique());
            if (existingActive.isPresent() && !existingActive.get().getId().equals(maquette.getId())) {
                throw new IllegalArgumentException("Une maquette active existe déjà pour cette formation et année académique");
            }
        }

        // Vérifier que la formation existe
        if (maquette.getFormation() != null && maquette.getFormation().getId() != null) {
            formationService.getFormationById(maquette.getFormation().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Formation non trouvée"));
        }

        return maquetteRepository.save(maquette);
    }

    private void validateMaquette(Maquette maquette) {
        if (maquette.getLibelle() == null || maquette.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé de la maquette est obligatoire");
        }
        if (maquette.getFormation() == null) {
            throw new IllegalArgumentException("La formation est obligatoire");
        }
        if (maquette.getAnneeAcademique() < 2000 || maquette.getAnneeAcademique() > 2100) {
            throw new IllegalArgumentException("L'année académique n'est pas valide");
        }
    }

    @Transactional
    public void deleteMaquette(Long id) {
        if (!maquetteRepository.existsById(id)) {
            throw new RuntimeException("Maquette introuvable avec l'ID: " + id);
        }
        maquetteRepository.deleteById(id);
    }

    @Transactional
    public void activateMaquette(Long id) {
        Maquette maquette = maquetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maquette introuvable"));

        // Désactiver toutes les autres maquettes de la même formation
        List<Maquette> autresMaquettes = maquetteRepository.findByFormationId(maquette.getFormation().getId());
        for (Maquette m : autresMaquettes) {
            if (!m.getId().equals(id)) {
                m.setActive(false);
                maquetteRepository.save(m);
            }
        }

        maquette.setActive(true);
        maquetteRepository.save(maquette);
    }
}