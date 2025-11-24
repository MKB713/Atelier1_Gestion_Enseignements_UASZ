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
		// Cette méthode sera exécutée après le démarrage de l'application
		// Ici, on crée un utilisateur admin par défaut si il n'existe pas déjà
		// Vous pouvez choisir les infos de votre choix
		if (utilisateurRepository.findByEmail("admin1@uasz.com").isEmpty()) {
			String password = "admin123";
			Utilisateur admin = new Utilisateur();
			admin.setNom("Admin");
			admin.setPrenom("UASZ");
			admin.setEmail("admin1@uasz.com");
			admin.setTelephone("763459000");
			admin.setRole(Role.ADMIN);
			admin.setEtat(Etat.ACTIF);
			admin.setPassword(new BCryptPasswordEncoder().encode(password));

			utilisateurRepository.save(admin);

			System.out.println("===== ADMIN CREE =====");
			System.out.println("Email : "  + admin.getEmail());
			System.out.println("Mot de passe : " + password);

		}
	}
}
