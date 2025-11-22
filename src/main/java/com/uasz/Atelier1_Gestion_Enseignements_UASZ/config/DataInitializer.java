package com.uasz.Atelier1_Gestion_Enseignements_UASZ.config;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.*;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmploiRepository emploiRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EcRepository ecRepository;

    @Autowired
    private RepartitionRepository repartitionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialiser les Emplois
        if (emploiRepository.count() == 0) {
            Emploi emploi1 = new Emploi();
            emploi1.setLibelle("Emploi du Temps Semestre 1");
            emploiRepository.save(emploi1);

            Emploi emploi2 = new Emploi();
            emploi2.setLibelle("Emploi du Temps Semestre 2");
            emploiRepository.save(emploi2);
        }

        // Initialiser les Bâtiments
        if (batimentRepository.count() == 0) {
            Batiment batimentA = new Batiment();
            batimentA.setLibelle("Bâtiment A");
            batimentA.setDescription("Bâtiment principal des sciences");
            batimentRepository.save(batimentA);

            Batiment batimentB = new Batiment();
            batimentB.setLibelle("Bâtiment B");
            batimentB.setDescription("Bâtiment des arts et humanités");
            batimentRepository.save(batimentB);
        }

        // Initialiser les Salles
        if (salleRepository.count() == 0) {
            List<Batiment> batiments = batimentRepository.findAll();
            if (!batiments.isEmpty()) {
                Salle salle101 = new Salle();
                salle101.setLibelle("Salle 101");
                salle101.setCapacite(50);
                salle101.setBatiment(batiments.get(0));
                salleRepository.save(salle101);

                Salle salle102 = new Salle();
                salle102.setLibelle("Salle 102");
                salle102.setCapacite(50);
                salle102.setBatiment(batiments.get(0));
                salleRepository.save(salle102);

                if (batiments.size() > 1) {
                    Salle salle201 = new Salle();
                    salle201.setLibelle("Salle 201");
                    salle201.setCapacite(40);
                    salle201.setBatiment(batiments.get(1));
                    salleRepository.save(salle201);
                }
            }
        }

        // Initialiser les Enseignants
        if (enseignantRepository.count() == 0) {
            Enseignant enseignant1 = new Enseignant();
            enseignant1.setMatricule(20240001L);
            enseignant1.setNom("Diop");
            enseignant1.setPrenom("Modou");
            enseignant1.setEmail("modou.diop@uasz.sn");
            enseignant1.setEstActif(true);
            enseignantRepository.save(enseignant1);

            Enseignant enseignant2 = new Enseignant();
            enseignant2.setMatricule(20240002L);
            enseignant2.setNom("Fall");
            enseignant2.setPrenom("Aissatou");
            enseignant2.setEmail("aissatou.fall@uasz.sn");
            enseignant2.setEstActif(true);
            enseignantRepository.save(enseignant2);
        }

        // Initialiser les EC (Matières)
        if (ecRepository.count() == 0) {
            Ec ec1 = new Ec();
            ec1.setLibelle("Mathématiques");
            ecRepository.save(ec1);

            Ec ec2 = new Ec();
            ec2.setLibelle("Informatique Fondamentale");
            ecRepository.save(ec2);
        }

        // Initialiser les Repartitions
        if (repartitionRepository.count() == 0) {
            List<Enseignant> enseignants = enseignantRepository.findAll();
            List<Ec> ecs = ecRepository.findAll();
            if (!enseignants.isEmpty() && !ecs.isEmpty()) {
                Repartition repartition1 = new Repartition();
                repartition1.setEnseignant(enseignants.get(0));
                repartition1.setEc(ecs.get(0)); // Diop - Mathématiques
                repartitionRepository.save(repartition1);

                Repartition repartition2 = new Repartition();
                repartition2.setEnseignant(enseignants.get(1));
                repartition2.setEc(ecs.get(1)); // Fall - Informatique
                repartitionRepository.save(repartition2);

                Repartition repartition3 = new Repartition();
                repartition3.setEnseignant(enseignants.get(0));
                repartition3.setEc(ecs.get(1)); // Diop - Informatique
                repartitionRepository.save(repartition3);
            }
        }
    }
}
