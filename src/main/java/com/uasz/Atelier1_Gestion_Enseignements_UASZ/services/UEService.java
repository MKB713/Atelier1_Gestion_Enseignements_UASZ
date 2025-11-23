package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.UE;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.UERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UEService {

    @Autowired
    private UERepository ueRepository;

    // --- LECTURE ---
    public List<UE> getAllUEs() {
        return ueRepository.findByArchive(false); // Uniquement les actifs
    }

    public List<UE> getArchivedUEs() {
        return ueRepository.findByArchive(true); // Uniquement les archives
    }

    public UE getUEById(Long id) {
        return ueRepository.findById(id).orElse(null);
    }

    // --- ECRITURE ---
    @Transactional
    public UE saveUE(UE ue) {
        // Création
        if (ue.getId() == null) {
            if (ueRepository.findByCode(ue.getCode()).isPresent()) {
                throw new IllegalArgumentException("Ce code d'UE existe déjà.");
            }
            ue.setDateCreation(new Date());
            ue.setActive(true);
            ue.setArchive(false);
        }
        // Modification (on récupère l'existant pour ne pas écraser la date ou l'état)
        else {
            UE existing = getUEById(ue.getId());
            if(existing != null) {
                ue.setDateCreation(existing.getDateCreation());
                ue.setActive(existing.isActive());
                ue.setArchive(existing.isArchive());
            }
        }
        return ueRepository.save(ue);
    }

    // --- ACTIONS ---
    @Transactional
    public void activer(Long id) {
        UE ue = getUEById(id);
        if(ue != null) { ue.setActive(true); ueRepository.save(ue); }
    }

    @Transactional
    public void desactiver(Long id) {
        UE ue = getUEById(id);
        if(ue != null) { ue.setActive(false); ueRepository.save(ue); }
    }

    @Transactional
    public void archiver(Long id) {
        UE ue = getUEById(id);
        if(ue != null) {
            ue.setArchive(true);
            ue.setActive(false); // Désactiver aussi
            ueRepository.save(ue);
        }
    }

    @Transactional
    public void restaurer(Long id) {
        UE ue = getUEById(id);
        if(ue != null) {
            ue.setArchive(false);
            ue.setActive(true); // Réactiver
            ueRepository.save(ue);
        }
    }
}