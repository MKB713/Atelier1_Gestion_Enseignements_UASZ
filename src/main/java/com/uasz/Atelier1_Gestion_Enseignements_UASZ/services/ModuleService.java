package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    // --- LECTURE ---

    public List<Module> getAllModules() {
        // Retourne uniquement les modules actifs (non archivés)
        return moduleRepository.findAll().stream()
                .filter(m -> !m.isArchive())
                .collect(Collectors.toList());
    }

    public List<Module> getArchivedModules() {
        // Retourne uniquement les archives
        return moduleRepository.findAll().stream()
                .filter(Module::isArchive)
                .collect(Collectors.toList());
    }

    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }

    // --- ECRITURE ---

    @Transactional
    public Module addModule(Module module) {
        module.setArchive(false); // Toujours actif à la création
        return moduleRepository.save(module);
    }

    @Transactional
    public Module updateModule(Long id, Module moduleDetails) {
        Module module = getModuleById(id);
        if (module != null) {
            module.setCode(moduleDetails.getCode());
            module.setLibelle(moduleDetails.getLibelle());
            module.setCycle(moduleDetails.getCycle());
            module.setNiveau(moduleDetails.getNiveau());
            // On ne touche pas à l'état archive ici
            return moduleRepository.save(module);
        }
        return null;
    }

    // --- GESTION DES ÉTATS (ARCHIVAGE) ---

    @Transactional
    public void archiveModule(Long id) {
        Module module = getModuleById(id);
        if (module != null) {
            module.setArchive(true);
            moduleRepository.save(module);
        }
    }

    @Transactional
    public void unarchiveModule(Long id) {
        Module module = getModuleById(id);
        if (module != null) {
            module.setArchive(false);
            moduleRepository.save(module);
        }
    }
}