package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.ECRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ECService {

    @Autowired
    private ECRepository ecRepository;

    public List<EC> getAllECs() {
        return ecRepository.findByArchive(false);
    }

    public List<EC> getArchivedECs() {
        return ecRepository.findByArchive(true);
    }

    public EC getECById(Long id) {
        return ecRepository.findById(id).orElse(null);
    }

    public EC addEC(EC ec) {
        return ecRepository.save(ec);
    }

    public EC updateEC(Long id, EC ecDetails) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setCode(ecDetails.getCode());
            ec.setLibelle(ecDetails.getLibelle());
            ec.setCredit(ecDetails.getCredit());
            ec.setHeureCm(ecDetails.getHeureCm());
            ec.setHeureTd(ecDetails.getHeureTd());
            ec.setHeureTp(ecDetails.getHeureTp());
            ec.setCoefficient(ecDetails.getCoefficient());
            ec.setTpe(ecDetails.getTpe());
            ec.setModule(ecDetails.getModule());
            return ecRepository.save(ec);
        }
        return null;
    }

    public void archiveEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setArchive(true);
            ecRepository.save(ec);
        }
    }

    public void unarchiveEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setArchive(false);
            ecRepository.save(ec);
        }
    }

    public void activateEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setActif(true);
            ecRepository.save(ec);
        }
    }

    public void deactivateEC(Long id) {
        EC ec = getECById(id);
        if (ec != null) {
            ec.setActif(false);
            ecRepository.save(ec);
        }
    }

    public List<EC> searchECs(String keyword) {
        return ecRepository.findByLibelleContainingIgnoreCaseAndArchive(keyword, false);
    }
}
