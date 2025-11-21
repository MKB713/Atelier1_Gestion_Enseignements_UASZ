package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private FormationService formationService;

    @Autowired
    private MaquetteService maquetteService;

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Optional<Module> getModuleById(Long id) {
        return moduleRepository.findById(id);
    }

    public List<Module> getModulesByFormation(Long formationId) {
        return moduleRepository.findByFormationId(formationId);
    }

    public List<Module> getModulesByMaquette(Long maquetteId) {
        return moduleRepository.findByMaquetteId(maquetteId);
    }

    public List<Module> getModulesBySemestreAndFormation(int semestre, Long formationId) {
        return moduleRepository.findBySemestreAndFormationId(semestre, formationId);
    }

    public List<Module> searchByLibelle(String libelle) {
        return moduleRepository.findByLibelleContainingIgnoreCase(libelle);
    }

    @Transactional
    public Module saveModule(Module module) {
        validateModule(module);

        if (module.getId() == null) {
            if (moduleRepository.existsByCode(module.getCode())) {
                throw new IllegalArgumentException("Un module avec le code " + module.getCode() + " existe déjà");
            }
        } else {
            Optional<Module> existingModule = moduleRepository.findByCode(module.getCode());
            if (existingModule.isPresent() && !existingModule.get().getId().equals(module.getId())) {
                throw new IllegalArgumentException("Un autre module utilise déjà le code " + module.getCode());
            }
        }

        // Vérifier que la formation existe
        if (module.getFormation() != null && module.getFormation().getId() != null) {
            formationService.getFormationById(module.getFormation().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Formation non trouvée"));
        }

        // Vérifier que la maquette existe (si spécifiée)
        if (module.getMaquette() != null && module.getMaquette().getId() != null) {
            maquetteService.getMaquetteById(module.getMaquette().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Maquette non trouvée"));
        }

        return moduleRepository.save(module);
    }

    private void validateModule(Module module) {
        if (module.getCode() == null || module.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Le code du module est obligatoire");
        }
        if (module.getLibelle() == null || module.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé du module est obligatoire");
        }
        if (module.getFormation() == null) {
            throw new IllegalArgumentException("La formation est obligatoire");
        }
        if (module.getSemestre() < 1 || module.getSemestre() > 2) {
            throw new IllegalArgumentException("Le semestre doit être 1 ou 2");
        }
    }

    @Transactional
    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new RuntimeException("Module introuvable avec l'ID: " + id);
        }
        moduleRepository.deleteById(id);
    }
}