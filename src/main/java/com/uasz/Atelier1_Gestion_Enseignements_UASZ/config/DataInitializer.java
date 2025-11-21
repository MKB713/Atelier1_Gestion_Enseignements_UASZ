package com.uasz.Atelier1_Gestion_Enseignements_UASZ.config;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.EC;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Module;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Cycle;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Niveau;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ECService;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ECService ecService;

    @Autowired
    private ModuleService moduleService;

    @Override
    public void run(String... args) throws Exception {
        if (moduleService.getAllModules().isEmpty()) {
            // ========== MODULES LICENCE L1 ==========
            Module module1 = new Module(null, "INFO101", "Programmation",
                    Cycle.LICENCE, Niveau.L1, true, new ArrayList<>());
            moduleService.addModule(module1);

            Module module2 = new Module(null, "MAT101", "Mathématiques de Base",
                    Cycle.LICENCE, Niveau.L1, true, new ArrayList<>());
            moduleService.addModule(module2);

            Module module3 = new Module(null, "PHY101", "Physique Générale",
                    Cycle.LICENCE, Niveau.L1, true, new ArrayList<>());
            moduleService.addModule(module3);

            // ========== MODULES LICENCE L2 ==========
            Module module4 = new Module(null, "INFO201", "Structure de Données",
                    Cycle.LICENCE, Niveau.L2, true, new ArrayList<>());
            moduleService.addModule(module4);

            Module module5 = new Module(null, "MAT201", "Analyse 2",
                    Cycle.LICENCE, Niveau.L2, true, new ArrayList<>());
            moduleService.addModule(module5);

            // ========== MODULES LICENCE L3 ==========
            Module module6 = new Module(null, "INFO301", "Bases de Données",
                    Cycle.LICENCE, Niveau.L3, true, new ArrayList<>());
            moduleService.addModule(module6);

            // ========== MODULES MASTER M1 ==========
            Module module7 = new Module(null, "INFO501", "Génie Logiciel",
                    Cycle.MASTER, Niveau.M1, true, new ArrayList<>());
            moduleService.addModule(module7);

            // ========== MODULES MASTER M2 ==========
            Module module8 = new Module(null, "INFO601", "Systèmes Distribués",
                    Cycle.MASTER, Niveau.M2, true, new ArrayList<>());
            moduleService.addModule(module8);

            // ========== MODULES DOCTORAT ==========
            Module module9 = new Module(null, "INFO801", "Recherche Avancée",
                    Cycle.DOCTORAT, Niveau.D1, true, new ArrayList<>());
            moduleService.addModule(module9);

            if (ecService.getAllECs().isEmpty()) {
                // ========== ECs LICENCE L1 ==========
                ecService.addEC(new EC(null, "INF101", "Algorithmique et Programmation", 5, 20, 10, 10, 3, 10, false, true, module1));
                ecService.addEC(new EC(null, "PROG101", "Introduction à Java", 4, 16, 8, 8, 2, 8, false, true, module1));
                ecService.addEC(new EC(null, "MAT101", "Analyse 1", 5, 20, 10, 0, 4, 0, false, true, module2));
                ecService.addEC(new EC(null, "ALGE101", "Algèbre Linéaire", 4, 16, 8, 0, 3, 0, false, true, module2));
                ecService.addEC(new EC(null, "PHY101", "Mécanique du point", 4, 20, 10, 5, 2, 0, false, true, module3));
                ecService.addEC(new EC(null, "CHI101", "Chimie générale", 4, 20, 10, 5, 1, 0, false, false, module3));
                ecService.addEC(new EC(null, "FRA101", "Techniques d'expression française", 2, 10, 10, 0, 1, 0, true, true, module1));

                // ========== ECs LICENCE L2 ==========
                ecService.addEC(new EC(null, "INFO201", "Structures de Données Avancées", 5, 20, 10, 10, 3, 10, false, true, module4));
                ecService.addEC(new EC(null, "ALGO201", "Complexité et Algorithmes", 4, 16, 8, 0, 3, 0, false, true, module4));
                ecService.addEC(new EC(null, "MAT201", "Analyse 2", 5, 20, 10, 0, 4, 0, false, true, module5));
                ecService.addEC(new EC(null, "PROB201", "Probabilités et Statistiques", 4, 16, 8, 0, 3, 5, false, true, module5));

                // ========== ECs LICENCE L3 ==========
                ecService.addEC(new EC(null, "BD301", "Bases de Données Relationnelles", 5, 20, 10, 10, 3, 10, false, true, module6));
                ecService.addEC(new EC(null, "SQL301", "SQL Avancé et Optimisation", 4, 16, 8, 8, 2, 5, false, true, module6));
                ecService.addEC(new EC(null, "WEB301", "Développement Web - Backend", 5, 16, 8, 16, 3, 12, false, true, module6));

                // ========== ECs MASTER M1 ==========
                ecService.addEC(new EC(null, "GL501", "Génie Logiciel - Fondamentaux", 5, 20, 10, 10, 4, 15, false, true, module7));
                ecService.addEC(new EC(null, "UML501", "UML et Design Patterns", 4, 16, 8, 0, 3, 10, false, true, module7));
                ecService.addEC(new EC(null, "AGILE501", "Méthodes Agiles et Scrum", 3, 12, 6, 0, 2, 8, false, true, module7));

                // ========== ECs MASTER M2 ==========
                ecService.addEC(new EC(null, "SD601", "Systèmes Distribués et Cloud", 5, 20, 10, 10, 4, 15, false, true, module8));
                ecService.addEC(new EC(null, "MICRO601", "Microservices Architecture", 4, 16, 8, 8, 3, 12, false, true, module8));
                ecService.addEC(new EC(null, "CLOUD601", "Cloud Computing - AWS/Azure", 5, 16, 8, 16, 3, 15, false, true, module8));

                // ========== ECs DOCTORAT ==========
                ecService.addEC(new EC(null, "REC801", "Recherche en Informatique", 6, 24, 12, 0, 5, 20, false, true, module9));
                ecService.addEC(new EC(null, "SEMI801", "Séminaire de Recherche", 3, 12, 0, 0, 2, 10, false, true, module9));
            }
        }
    }
}
