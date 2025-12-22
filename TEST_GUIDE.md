# 🧪 Guide de Test - Intégration Enseignant Pages

Ce guide vous aide à tester complètement l'intégration des pages enseignant dans auth-service.

## 📋 Pré-requis

- ✅ Tous les services sont compilés avec `mvn clean package`
- ✅ MySQL est en cours d'exécution
- ✅ Les bases de données sont créées
- ✅ Eureka Server est en cours d'exécution sur le port 8761
- ✅ Enseignant-Service est en cours d'exécution sur le port 8082
- ✅ Auth-Service est en cours d'exécution sur le port 8081

## 🚀 Procédure de Démarrage

### 1. Démarrer Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
✅ Vérifier: http://localhost:8761 (vous devriez voir la page d'accueil Eureka)

### 2. Démarrer Enseignant-Service
```bash
cd enseignant-service
mvn spring-boot:run
```
✅ Vérifier les logs: "Started EnseignantServiceApplication"
✅ Vérifier dans Eureka: http://localhost:8761 - ENSEIGNANT-SERVICE doit être enregistré

### 3. Démarrer Auth-Service
```bash
cd auth-service
mvn spring-boot:run
```
✅ Vérifier les logs: "Started AuthServiceApplication"
✅ Vérifier dans Eureka: http://localhost:8761 - AUTH-SERVICE doit être enregistré

---

## ✅ Test 1: Accès aux Pages

### 1.1 Dashboard Enseignant
**URL**: http://localhost:8081/dashboard/enseignant

**Étapes**:
1. Ouvrez l'URL
2. Vous devriez voir:
   - ✅ Espace Enseignant (titre principal)
   - ✅ Sections: Emplois du Temps, Cahier de Texte, Formations, etc.
   - ✅ **NOUVEAU**: Section "Administration" avec:
     - "Enseignants Actifs" (badge 🟡)
     - "Nouvel Enseignant" (badge 🟢)
     - "Archives" (badge ⚫)

### 1.2 Page d'Authentification
**URL**: http://localhost:8081/login

**Étapes**:
1. Si vous n'êtes pas connecté, vous serrez redirigé vers le login
2. Connectez-vous avec vos identifiants

**Identifiants de test** (si existants):
- Username: `enseignant@uasz.sn` / Password: `password`
- Ou utilisez vos propres identifiants

---

## ✅ Test 2: Liste des Enseignants

**URL**: http://localhost:8081/lst-enseignants

**Étapes**:
1. Accédez à la page
2. Vérifiez que:
   - ✅ Le titre affiche "Liste des Enseignants"
   - ✅ Un bouton "Nouvel Enseignant" en haut à droite
   - ✅ Un champ de recherche fonctionne
   - ✅ Si des enseignants existent, ils s'affichent dans un tableau
   - ✅ Le tableau a les colonnes: Enseignant, Grade & Spécialité, Contact, Statut, Actions
   - ✅ Un bouton "Accéder aux Archives" en bas à gauche

**Vérifier la recherche**:
1. Tapez un nom dans la boîte de recherche
2. La liste doit se filtrer en temps réel

**Vérifier les actions** (si enseignants présents):
1. Cliquez sur le menu déroulant (⋮) dans la colonne Actions
2. Options disponibles:
   - ✅ Voir le dossier
   - ✅ Modifier
   - ✅ Désactiver/Réactiver
   - ✅ Archiver

---

## ✅ Test 3: Ajouter un Enseignant

**URL**: http://localhost:8081/add-enseignant

**Étapes**:
1. Accédez à la page
2. Le formulaire doit avoir:
   - ✅ Section "Grade & Statut" avec selects
   - ✅ Section "Informations Personnelles" (Nom, Prénom, Date, Lieu)
   - ✅ Section "Coordonnées & Profession" (Email, Tél, Adresse, Spécialité)
   - ✅ Case à cocher "Activer ce compte enseignant immédiatement"
   - ✅ Boutons "Annuler" et "Enregistrer"

**Ajouter un enseignant**:
1. Remplissez le formulaire avec:
   - **Nom**: `TestDurant`
   - **Prénom**: `Malick`
   - **Date de naissance**: `1985-05-15`
   - **Lieu**: `Dakar`
   - **Grade**: `Maître-Assistant`
   - **Statut**: `PERMANENT`
   - **Email**: `malick.durant@univ-zig.sn`
   - **Téléphone**: `+221771234567`
   - **Adresse**: `Ziguinchor, Sénégal`
   - **Spécialité**: `Informatique`
   - **Date d'embauche**: `2020-01-15`
   
2. Cliquez sur "Enregistrer"

**Résultat attendu**:
- ✅ Redirection vers `/lst-enseignants`
- ✅ Message de succès affiché
- ✅ L'enseignant apparaît dans la liste
- ✅ Un matricule a été généré automatiquement

---

## ✅ Test 4: Modifier un Enseignant

**URL**: http://localhost:8081/edit-enseignant/{id}

**Étapes**:
1. Allez sur `/lst-enseignants`
2. Trouvez l'enseignant que vous venez d'ajouter
3. Cliquez sur le menu (⋮) → "Modifier"
4. Le formulaire doit:
   - ✅ Afficher le matricule (en lecture seule)
   - ✅ Afficher les données actuelles de l'enseignant
   - ✅ Permettre de modifier chaque champ

5. Modifiez un champ (ex: spécialité)
6. Cliquez sur "Enregistrer les modifications"

**Résultat attendu**:
- ✅ Redirection vers `/lst-enseignants`
- ✅ Message de succès
- ✅ Les modifications sont visibles dans la liste

---

## ✅ Test 5: Voir les Détails

**URL**: http://localhost:8081/view-enseignant/{id}

**Étapes**:
1. Allez sur `/lst-enseignants`
2. Cliquez sur le menu (⋮) → "Voir le dossier"
3. La page doit afficher:
   - ✅ Avatar circulaire avec les initiales
   - ✅ Nom et prénom
   - ✅ Grade
   - ✅ Badges (Actif/Inactif, Permanent/Vacataire)
   - ✅ Bouton "Modifier le dossier"
   - ✅ Informations détaillées:
     - État Civil (Date, Lieu de naissance)
     - Carrière (Spécialité, Date d'embauche)
     - Coordonnées (Email, Téléphone, Adresse)
   - ✅ Date de création
   - ✅ Bouton "Archiver ce dossier"

---

## ✅ Test 6: Gestion du Statut

### 6.1 Désactiver un Enseignant
**Étapes**:
1. Allez sur `/lst-enseignants`
2. Trouvez un enseignant actif
3. Cliquez sur le menu (⋮) → "Désactiver"
4. Confirmez l'action

**Résultat attendu**:
- ✅ Le badge passe de "Actif" à "Inactif"
- ✅ Message de succès affiché

### 6.2 Réactiver un Enseignant
**Étapes**:
1. Dans `/lst-enseignants`, trouvez un enseignant inactif
2. Cliquez sur le menu (⋮) → "Réactiver"

**Résultat attendu**:
- ✅ Le badge repasse à "Actif"
- ✅ Message de succès

---

## ✅ Test 7: Archivage

### 7.1 Archiver un Enseignant
**Étapes**:
1. Allez sur `/lst-enseignants`
2. Cliquez sur le menu (⋮) → "Archiver" pour un enseignant
3. Confirmez avec "Êtes-vous sûr ?"

**Résultat attendu**:
- ✅ L'enseignant disparaît de la liste
- ✅ Message de succès
- ✅ Redirection vers `/lst-enseignants`

### 7.2 Voir les Archives
**Étapes**:
1. Allez sur `/lst-enseignants-archives`
2. L'enseignant archivé doit:
   - ✅ Apparaître dans la liste des archives
   - ✅ Avoir un avatar gris (au lieu de vert)

### 7.3 Désarchiver
**Étapes**:
1. Dans `/lst-enseignants-archives`
2. Cliquez sur le menu (⋮) → "Désarchiver"

**Résultat attendu**:
- ✅ Redirection vers `/lst-enseignants-archives`
- ✅ Message de succès
- ✅ L'enseignant réapparaît dans `/lst-enseignants`

---

## ✅ Test 8: API REST Directe

### 8.1 Récupérer tous les enseignants
```bash
curl -X GET http://localhost:8082/api/enseignants
```

**Résultat attendu**:
- ✅ Code 200
- ✅ JSON array avec les enseignants

### 8.2 Récupérer un enseignant par ID
```bash
curl -X GET http://localhost:8082/api/enseignants/1
```

**Résultat attendu**:
- ✅ Code 200
- ✅ JSON objet avec les détails de l'enseignant

### 8.3 Archiver via API
```bash
curl -X POST http://localhost:8082/api/enseignants/1/archive
```

**Résultat attendu**:
- ✅ Code 200
- ✅ L'enseignant est archivé

---

## 📊 Checklist de Test Complète

| Test | URL | Résultat | Status |
|---|---|---|---|
| Dashboard Enseignant | `/dashboard/enseignant` | Page affichée avec section Admin | ⬜ |
| Liste des enseignants | `/lst-enseignants` | Tableau affiché | ⬜ |
| Recherche | `/lst-enseignants` | Recherche en temps réel | ⬜ |
| Ajouter enseignant | `/add-enseignant` | Formulaire affiché | ⬜ |
| Sauvegarder | POST `/save-enseignant` | Enseignant créé | ⬜ |
| Modifier | `/edit-enseignant/{id}` | Formulaire de modification | ⬜ |
| Mettre à jour | POST `/update-enseignant/{id}` | Modifications sauvegardées | ⬜ |
| Voir détails | `/view-enseignant/{id}` | Dossier complet affiché | ⬜ |
| Désactiver | POST `/desactiver-enseignant/{id}` | Badge changé en Inactif | ⬜ |
| Activer | POST `/activer-enseignant/{id}` | Badge changé en Actif | ⬜ |
| Archiver | POST `/archive-enseignant/{id}` | Enseignant disparu de la liste | ⬜ |
| Archives | `/lst-enseignants-archives` | Archives affichées | ⬜ |
| Désarchiver | POST `/unarchive-enseignant/{id}` | Enseignant réapparait | ⬜ |
| API - GET tous | `/api/enseignants` | JSON retourné | ⬜ |
| API - GET un | `/api/enseignants/{id}` | JSON objet retourné | ⬜ |

---

## 🔍 Troubleshooting

### Erreur: "Impossible de charger la liste des enseignants"

**Solutions**:
1. Vérifiez que `enseignant-service` est démarré
2. Vérifiez les logs du service pour les erreurs
3. Vérifiez dans Eureka: http://localhost:8761
4. Vérifiez la connexion à la base de données

### Erreur 404 sur `/api/enseignants`

**Solutions**:
1. Vérifiez que `EnseignantRestController` existe
2. Vérifiez l'annotation `@RestController` et `@RequestMapping`
3. Vérifiez le port du service (8082)

### Les données ne persistent pas

**Solutions**:
1. Vérifiez que MySQL est en cours d'exécution
2. Vérifiez les logs pour les erreurs de base de données
3. Vérifiez les propriétés de connexion dans `application.properties`

### La page reste vide

**Solutions**:
1. Vérifiez les logs du navigateur (F12 → Console)
2. Vérifiez les logs du serveur
3. Vérifiez que Eureka a enregistré les services

---

## 📝 Notes

- Les tests doivent être exécutés dans l'ordre donné
- Utilisez un navigateur moderne (Chrome, Firefox, Edge)
- Nettoyez les données de test après les tests
- Consultez les logs pour déboguer les problèmes

---

**Résultat de test attendu**: ✅ Tous les tests passent avec succès!
