package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ECRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ECService {

    @Autowired
    private ECRepository ecRepository;

    public List<EC> getAllECs() {
        // Retourne uniquement les ECs non archivés
        return ecRepository.findByArchive(false);
    }

    public List<EC> getArchivedECs() {
        return ecRepository.findByArchive(true);
    }

    public EC getECById(Long id) {
        return ecRepository.findById(id).orElse(null);
    }

    @Transactional
    public EC addEC(EC ec) {
        ec.setArchive(false);
        ec.setActif(true); // Par défaut actif à la création
        return ecRepository.save(ec);
    }

    @Transactional
    public EC updateEC(Long id, EC ecDetails) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setCode(ecDetails.getCode());
            ec.setLibelle(ecDetails.getLibelle());
            ec.setModule(ecDetails.getModule());

            // Mise à jour des heures et coefficients
            ec.setHeureCm(ecDetails.getHeureCm());
            ec.setHeureTd(ecDetails.getHeureTd());
            ec.setHeureTp(ecDetails.getHeureTp());
            ec.setCoefficient(ecDetails.getCoefficient());
            ec.setCredit(ecDetails.getCredit());
            ec.setTpe(ecDetails.getTpe());

            return ecRepository.save(ec);
        }
        return null;
    }

    // --- GESTION DES ÉTATS ---

    @Transactional
    public void activateEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setActif(true);
            ecRepository.save(ec);
        }
    }

    @Transactional
    public void deactivateEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setActif(false);
            ecRepository.save(ec);
        }
    }

    @Transactional
    public void archiveEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setArchive(true);
            ec.setActif(false); // On désactive aussi quand on archive
            ecRepository.save(ec);
        }
    }

    @Transactional
    public void unarchiveEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setArchive(false);
            ec.setActif(true); // On réactive à la restauration
            ecRepository.save(ec);
        }
    }
}