package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }

    public Module addModule(Module module) {
        return moduleRepository.save(module);
    }

    public Module updateModule(Long id, Module moduleDetails) {
        Module module = getModuleById(id);
        if (module != null) {
            module.setCode(moduleDetails.getCode());
            module.setLibelle(moduleDetails.getLibelle());
            module.setCycle(moduleDetails.getCycle());
            module.setNiveau(moduleDetails.getNiveau());
            return moduleRepository.save(module);
        }
        return null;
    }

    public void deleteModule(Long id) {
        moduleRepository.deleteById(id);
    }
}
