package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.UE;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.UERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UEService {

    @Autowired
    private UERepository ueRepository;

    @Autowired
    private FormationService formationService;

    @Autowired
    private ModuleService moduleService;

    public List<UE> getAllUEs() {
        return ueRepository.findAll();
    }

    public Optional<UE> getUEById(Long id) {
        return ueRepository.findById(id);
    }

    public List<UE> getUEsByFormation(Long formationId) {
        return ueRepository.findByFormationId(formationId);
    }

    public List<UE> getUEsByModule(Long moduleId) {
        return ueRepository.findByModuleId(moduleId);
    }

    public List<UE> searchByLibelle(String libelle) {
        return ueRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    @Transactional
    public UE saveUE(UE ue) {
        validateUE(ue);

        if (ue.getId() == null) {
            if (ueRepository.existsByCode(ue.getCode())) {
                throw new IllegalArgumentException("Une UE avec le code " + ue.getCode() + " existe déjà");
            }
        } else {
            Optional<UE> existingUE = ueRepository.findByCode(ue.getCode());
            if (existingUE.isPresent() && !existingUE.get().getId().equals(ue.getId())) {
                throw new IllegalArgumentException("Une autre UE utilise déjà le code " + ue.getCode());
            }
        }

        // Vérifier que la formation existe
        if (ue.getFormation() != null && ue.getFormation().getId() != null) {
            formationService.getFormationById(ue.getFormation().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Formation non trouvée"));
        }

        // Vérifier que le module existe (si spécifié)
        if (ue.getModule() != null && ue.getModule().getId() != null) {
            moduleService.getModuleById(ue.getModule().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Module non trouvé"));
        }

        return ueRepository.save(ue);
    }

    private void validateUE(UE ue) {
        if (ue.getCode() == null || ue.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Le code de l'UE est obligatoire");
        }
        if (ue.getLibelle() == null || ue.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé de l'UE est obligatoire");
        }
        if (ue.getFormation() == null) {
            throw new IllegalArgumentException("La formation est obligatoire");
        }
        if (ue.getCredits() < 0) {
            throw new IllegalArgumentException("Le nombre de crédits ne peut pas être négatif");
        }
        if (ue.getVolumeHoraire() < 0) {
            throw new IllegalArgumentException("Le volume horaire ne peut pas être négatif");
        }
    }

    @Transactional
    public void deleteUE(Long id) {
        if (!ueRepository.existsById(id)) {
            throw new RuntimeException("UE introuvable avec l'ID: " + id);
        }
        ueRepository.deleteById(id);
    }
}