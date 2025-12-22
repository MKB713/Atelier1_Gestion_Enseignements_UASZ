import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ResetAdminPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Test plusieurs mots de passe possibles
        String[] possiblePasswords = {"password", "admin", "admin123", "123456"};
        String hashFromDB = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkK";

        System.out.println("=== Test des mots de passe possibles ===");
        for (String pwd : possiblePasswords) {
            boolean matches = encoder.matches(pwd, hashFromDB);
            System.out.println("Password: " + pwd + " -> " + (matches ? "MATCH!" : "No match"));
        }

        System.out.println("\n=== Nouveau hash pour 'admin123' ===");
        String newPassword = "admin123";
        String newHash = encoder.encode(newPassword);
        System.out.println("Nouveau mot de passe: " + newPassword);
        System.out.println("Hash à mettre dans la DB: " + newHash);

        System.out.println("\n=== Commande SQL de mise à jour ===");
        System.out.println("UPDATE daos_auth_db.utilisateur SET password = '" + newHash + "' WHERE email = 'admin@uasz.sn';");
    }
}
