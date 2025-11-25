package com.uasz.Atelier1_Gestion_Enseignements_UASZ;

import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Utilisateur;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Role;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.enums.Etat;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.repositories.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Atelier1GestionEnseignementsUaszApplication implements CommandLineRunner {

	private final UtilisateurRepository utilisateurRepository;

	public Atelier1GestionEnseignementsUaszApplication(UtilisateurRepository utilisateurRepository) {
		this.utilisateurRepository = utilisateurRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(Atelier1GestionEnseignementsUaszApplication.class, args);
	}

	@Override
	public void run(String... args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		System.out.println("\n========== CRÉATION DES UTILISATEURS DE TEST ==========\n");

		// ADMIN
		if (utilisateurRepository.findByEmail("admin1@uasz.com").isEmpty()) {
			Utilisateur admin = new Utilisateur();
			admin.setNom("Admin");
			admin.setPrenom("UASZ");
			admin.setEmail("admin1@uasz.com");
			admin.setTelephone("763459000");
			admin.setRole(Role.ADMIN);
			admin.setEtat(Etat.ACTIF);
			admin.setPassword(passwordEncoder.encode("admin123"));
			utilisateurRepository.save(admin);
			System.out.println("✓ ADMIN créé : admin1@uasz.com / admin123");
		}

		// ETUDIANT
		if (utilisateurRepository.findByEmail("etudiant@uasz.com").isEmpty()) {
			Utilisateur etudiant = new Utilisateur();
			etudiant.setNom("Diop");
			etudiant.setPrenom("Amadou");
			etudiant.setEmail("etudiant@uasz.com");
			etudiant.setTelephone("771234567");
			etudiant.setRole(Role.ETUDIANT);
			etudiant.setEtat(Etat.ACTIF);
			etudiant.setPassword(passwordEncoder.encode("etudiant123"));
			utilisateurRepository.save(etudiant);
			System.out.println("✓ ETUDIANT créé : etudiant@uasz.com / etudiant123");
		}

		// ENSEIGNANT
		if (utilisateurRepository.findByEmail("enseignant@uasz.com").isEmpty()) {
			Utilisateur enseignant = new Utilisateur();
			enseignant.setNom("Fall");
			enseignant.setPrenom("Fatou");
			enseignant.setEmail("enseignant@uasz.com");
			enseignant.setTelephone("772345678");
			enseignant.setRole(Role.ENSEIGNANT);
			enseignant.setEtat(Etat.ACTIF);
			enseignant.setPassword(passwordEncoder.encode("enseignant123"));
			utilisateurRepository.save(enseignant);
			System.out.println("✓ ENSEIGNANT créé : enseignant@uasz.com / enseignant123");
		}

		// RESPONSABLE MASTER
		if (utilisateurRepository.findByEmail("responsable@uasz.com").isEmpty()) {
			Utilisateur responsable = new Utilisateur();
			responsable.setNom("Sow");
			responsable.setPrenom("Moussa");
			responsable.setEmail("responsable@uasz.com");
			responsable.setTelephone("773456789");
			responsable.setRole(Role.RESPONSABLE_MASTER);
			responsable.setEtat(Etat.ACTIF);
			responsable.setPassword(passwordEncoder.encode("responsable123"));
			utilisateurRepository.save(responsable);
			System.out.println("✓ RESPONSABLE MASTER créé : responsable@uasz.com / responsable123");
		}

		// COORDINATEUR DES LICENCES
		if (utilisateurRepository.findByEmail("coordinateur@uasz.com").isEmpty()) {
			Utilisateur coordinateur = new Utilisateur();
			coordinateur.setNom("Ndiaye");
			coordinateur.setPrenom("Aissatou");
			coordinateur.setEmail("coordinateur@uasz.com");
			coordinateur.setTelephone("774567890");
			coordinateur.setRole(Role.COORDONATEUR_DES_LICENCES);
			coordinateur.setEtat(Etat.ACTIF);
			coordinateur.setPassword(passwordEncoder.encode("coordinateur123"));
			utilisateurRepository.save(coordinateur);
			System.out.println("✓ COORDINATEUR créé : coordinateur@uasz.com / coordinateur123");
		}

		// CHEF DE DEPARTEMENT
		if (utilisateurRepository.findByEmail("chef@uasz.com").isEmpty()) {
			Utilisateur chef = new Utilisateur();
			chef.setNom("Sarr");
			chef.setPrenom("Ibrahima");
			chef.setEmail("chef@uasz.com");
			chef.setTelephone("775678901");
			chef.setRole(Role.CHEF_DE_DEPARTEMENT);
			chef.setEtat(Etat.ACTIF);
			chef.setPassword(passwordEncoder.encode("chef123"));
			utilisateurRepository.save(chef);
			System.out.println("✓ CHEF DE DEPARTEMENT créé : chef@uasz.com / chef123");
		}

		System.out.println("\n========== UTILISATEURS CRÉÉS AVEC SUCCÈS ==========\n");
	}
}
