# 🔐 Comptes de Connexion - Portail Pédagogique UASZ

## 📌 Informations Générales

**URL d'accès au portail** : `http://localhost:8080/` (via API Gateway)
**URL directe auth-service** : `http://localhost:8081/`
**Mot de passe pour tous les comptes** : `password123`

---

## 👥 Comptes Créés

### 1️⃣ ADMINISTRATEUR
- **Rôle** : Accès complet à toutes les fonctionnalités
- **Email** : `admin@uasz.sn`
- **Mot de passe** : `password123`
- **Dashboard** : `/dashboard/admin`

---

### 2️⃣ CHEF DE DÉPARTEMENT
- **Rôle** : Accès administratif (équivalent Admin)
- **Nom** : Mamadou Diallo
- **Email** : `chef.departement@uasz.sn`
- **Mot de passe** : `password123`
- **Dashboard** : `/dashboard/admin`

---

### 3️⃣ COORDINATEUR DES LICENCES
- **Rôle** : Gestion complète des licences et formations
- **Nom** : Fatou Sarr
- **Email** : `coordinateur@uasz.sn`
- **Mot de passe** : `password123`
- **Dashboard** : `/dashboard/coordinateur`

---

### 4️⃣ RESPONSABLE MASTER
- **Rôle** : Gestion complète des masters
- **Nom** : Ousmane Ndiaye
- **Email** : `responsable.master@uasz.sn`
- **Mot de passe** : `password123`
- **Dashboard** : `/dashboard/responsable`

---

### 5️⃣ ENSEIGNANT
- **Rôle** : Consultation (formations, maquettes, cahier de texte, EDT)
- **Nom** : Cheikh Sall
- **Email** : `enseignant@uasz.sn`
- **Mot de passe** : `password123`
- **Dashboard** : `/dashboard/enseignant`

---

### 6️⃣ ÉTUDIANT
- **Rôle** : Consultation des emplois du temps uniquement
- **Nom** : Aminata Sow
- **Email** : `etudiant@uasz.sn`
- **Mot de passe** : `password123`
- **Dashboard** : `/dashboard/etudiant`

---

### 7️⃣ ÉTUDIANT 2 (Compte secondaire)
- **Rôle** : Consultation des emplois du temps uniquement
- **Nom** : Ibrahima Ba
- **Email** : `etudiant2@uasz.sn`
- **Mot de passe** : `password123`
- **Dashboard** : `/dashboard/etudiant`

---

## 🚀 Comment Tester

### Étape 1 : Démarrer les services

```bash
# Terminal 1 - Eureka Server
cd eureka-server
mvn spring-boot:run

# Terminal 2 - Auth Service
cd auth-service
mvn spring-boot:run

# Terminal 3 - API Gateway (optionnel)
cd api-gateway
mvn spring-boot:run
```

### Étape 2 : Accéder au portail

1. Ouvrez votre navigateur
2. Allez sur `http://localhost:8080/` ou `http://localhost:8081/`
3. Cliquez sur "Accéder au Portail"
4. Connectez-vous avec l'un des comptes ci-dessus

---

## 🔧 Gestion des Comptes

### Réinitialiser tous les utilisateurs de test

```bash
mysql -u root < init-users.sql
```

### Vérifier les utilisateurs dans la base de données

```bash
mysql -u root -e "SELECT id, CONCAT(prenom, ' ', nom) AS nom_complet, email, role, etat FROM daos_auth_db.utilisateur;"
```

### Générer un nouveau hash de mot de passe

```bash
cd auth-service
mvn compile exec:java -Dexec.mainClass="com.uasz.daos.auth.util.PasswordHashGenerator" -q
```

---

## 📋 Matrice des Permissions

| Rôle | Dashboard | Gestion Formations | Gestion Maquettes | Gestion EDT | Cahier de Texte | Gestion Utilisateurs |
|------|-----------|-------------------|-------------------|-------------|----------------|---------------------|
| **ADMIN** | ✅ Complet | ✅ | ✅ | ✅ | ✅ | ✅ |
| **CHEF_DE_DEPARTEMENT** | ✅ Complet | ✅ | ✅ | ✅ | ✅ | ✅ |
| **COORDONATEUR_DES_LICENCES** | ✅ Licences | ✅ Licences | ✅ Licences | ✅ | ✅ | ❌ |
| **RESPONSABLE_MASTER** | ✅ Masters | ✅ Masters | ✅ Masters | ✅ | ✅ | ❌ |
| **ENSEIGNANT** | 👁️ Consultation | 👁️ | 👁️ | 👁️ | ✅ Gestion | ❌ |
| **ETUDIANT** | 👁️ EDT | ❌ | ❌ | 👁️ | ❌ | ❌ |

**Légende** :
- ✅ = Accès complet (Création, Modification, Suppression)
- 👁️ = Consultation uniquement
- ❌ = Pas d'accès

---

## 📝 Notes Importantes

1. **Sécurité** : Ces comptes sont destinés au développement uniquement. En production, utilisez des mots de passe forts et uniques.

2. **Table Enseignant** : La table `enseignants` existe mais n'a pas de champ `password`. Les enseignants doivent être créés dans la table `utilisateur` avec le rôle `ENSEIGNANT` pour pouvoir se connecter.

3. **États des comptes** : Tous les comptes sont en état `ACTIF`. Les états possibles sont :
   - `ACTIF` : Compte actif, peut se connecter
   - `INACTIF` : Compte désactivé temporairement
   - `ARCHIVE` : Compte archivé

4. **Fichiers importants** :
   - `init-users.sql` : Script SQL pour créer tous les utilisateurs
   - `auth-service/src/main/java/com/uasz/daos/auth/util/PasswordHashGenerator.java` : Générateur de hash BCrypt
   - `auth-service/src/main/resources/data-test-users.sql` : Données de test alternatives

---

## 🐛 Dépannage

### Erreur : "Email ou mot de passe incorrect"
- Vérifiez que les utilisateurs sont bien dans la base de données
- Vérifiez que le mot de passe est bien `password123`
- Vérifiez que l'état du compte est `ACTIF`

### Erreur : "Page welcome.html ne s'affiche pas"
- Vérifiez que la dépendance Thymeleaf est dans le `pom.xml`
- Recompilez le projet : `mvn clean install`

### Erreur : "Cannot connect to database"
- Vérifiez que MySQL est démarré
- Vérifiez les credentials dans `application.properties`
- Créez la base si nécessaire : `CREATE DATABASE daos_auth_db;`

---

**Date de création** : 2024-12-19
**Version** : 1.0
