# Intégration des Pages HTML Enseignant-Service dans Auth-Service

## Aperçu
Ce document explique comment les pages HTML du `enseignant-service` sont désormais intégrées et accessibles à partir du `auth-service` via un système de proxy avec appels API REST.

## Architecture

### 1. **Components Auth-Service**

#### Contrôleur Proxy (EnseignantProxyController.java)
- **Localisation**: `auth-service/src/main/java/com/uasz/daos/auth/controller/EnseignantProxyController.java`
- **Rôle**: 
  - Intercepte les requêtes des utilisateurs pour les pages enseignant
  - Effectue des appels REST à `enseignant-service` via un `RestTemplate` équilibré par charge
  - Retourne les données au modèle Thymeleaf pour le rendu des pages

#### Configuration RestTemplate (RestTemplateConfig.java)
- **Localisation**: `auth-service/src/main/java/com/uasz/daos/auth/config/RestTemplateConfig.java`
- **Rôle**:
  - Configure un `RestTemplate` avec support du `@LoadBalanced`
  - Permet la communication avec `enseignant-service` via Eureka

#### Pages HTML/Thymeleaf
- **Localisation**: `auth-service/src/main/resources/templates/`
- **Pages intégrées**:
  - `enseignant-list.html` - Liste des enseignants actifs
  - `enseignant-add.html` - Formulaire d'ajout
  - `enseignant-edit.html` - Formulaire de modification
  - `enseignant-details.html` - Vue détail d'un enseignant
  - `enseignant-archive-list.html` - Liste des archives
  - `dashboard-enseignant.html` - Dashboard avec liens vers la gestion enseignant

### 2. **Components Enseignant-Service**

#### API REST Controller (EnseignantRestController.java)
- **Localisation**: `enseignant-service/src/main/java/com/uasz/daos/enseignant/controller/EnseignantRestController.java`
- **Endpoints**:
  ```
  GET    /api/enseignants              - Tous les enseignants actifs
  GET    /api/enseignants/archives     - Tous les enseignants archivés
  GET    /api/enseignants/{id}         - Détails d'un enseignant
  POST   /api/enseignants              - Créer un enseignant
  PUT    /api/enseignants/{id}         - Mettre à jour
  POST   /api/enseignants/{id}/archive - Archiver
  POST   /api/enseignants/{id}/unarchive - Désarchiver
  POST   /api/enseignants/{id}/activate - Activer
  POST   /api/enseignants/{id}/deactivate - Désactiver
  ```

#### Controller Existant (EnseignantController.java)
- Reste intact pour les appels directs au service
- Utilisé pour les tests internes

## Flux de Requête

```
1. Utilisateur clique sur "/lst-enseignants" dans le navigateur
   ↓
2. EnseignantProxyController.listEnseignants() est appelé
   ↓
3. RestTemplate effectue un appel GET vers "http://enseignant-service/api/enseignants"
   ↓
4. EnseignantRestController retourne la liste JSON
   ↓
5. Les données sont ajoutées au modèle Thymeleaf
   ↓
6. enseignant-list.html est rendu avec les données
   ↓
7. HTML final est retourné au navigateur
```

## Routes Disponibles via Auth-Service

### Consultation
- `GET /lst-enseignants` - Liste des enseignants actifs
- `GET /view-enseignant/{id}` - Détails d'un enseignant
- `GET /lst-enseignants-archives` - Liste des archives

### Création/Modification
- `GET /add-enseignant` - Formulaire d'ajout
- `POST /save-enseignant` - Enregistrer un nouvel enseignant
- `GET /edit-enseignant/{id}` - Formulaire de modification
- `POST /update-enseignant/{id}` - Mettre à jour

### Gestion du Statut
- `POST /archive-enseignant/{id}` - Archiver
- `POST /unarchive-enseignant/{id}` - Désarchiver
- `POST /activer-enseignant/{id}` - Activer
- `POST /desactiver-enseignant/{id}` - Désactiver

## Configuration Requise

### 1. **Dépendances Maven**
L'auth-service nécessite:
- `spring-cloud-starter-netflix-eureka-client` (déjà présent)
- Support de RestTemplate avec LoadBalancing

### 2. **Eureka Registration**
Les deux services doivent être enregistrés auprès d'Eureka:
- `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`
- `spring.application.name=enseignant-service` (ou auth-service)

### 3. **Ports**
- Auth-Service: `8081`
- Enseignant-Service: `8082`
- Eureka Server: `8761`

## Exemple d'Utilisation

### Pour accéder à la liste des enseignants:
1. Aller à `http://localhost:8081/dashboard/enseignant`
2. Cliquer sur "Enseignants Actifs" ou aller directement à `http://localhost:8081/lst-enseignants`

### Pour ajouter un enseignant:
1. Accéder à `http://localhost:8081/add-enseignant`
2. Remplir le formulaire
3. Cliquer sur "Enregistrer"
4. Les données sont envoyées à `enseignant-service` via une requête POST

### Pour modifier un enseignant:
1. Aller à la liste des enseignants
2. Cliquer sur "Modifier" dans le menu déroulant
3. L'URL sera `http://localhost:8081/edit-enseignant/{id}`
4. Effectuer les modifications et enregistrer

## Avantages de cette Architecture

✅ **Séparation des préoccupations**: UI centralisée dans auth-service, données dans enseignant-service
✅ **Réutilisabilité**: Les pages peuvent être utilisées par d'autres services
✅ **Scalabilité**: Chaque service peut être déployé indépendamment
✅ **Testabilité**: API REST facilite les tests d'intégration
✅ **Load Balancing**: Eureka permet la distribution de charge automatique

## Troubleshooting

### Erreur: "Impossible de charger la liste des enseignants"
- Vérifier que `enseignant-service` est démarré sur le port `8082`
- Vérifier que `eureka-server` est accessible à `http://localhost:8761`
- Vérifier les logs du `enseignant-service`

### 404 Not Found sur `/api/enseignants`
- S'assurer que `EnseignantRestController` est présent dans le classpath
- Vérifier l'annotation `@RestController` et `@RequestMapping("/api/enseignants")`

### Les données ne sont pas mises à jour
- Vérifier que les POST/PUT sont bien routés vers l'API REST
- Consulter les logs de la console pour les erreurs d'interaction avec la base de données

## Fichiers Modifiés/Créés

### Fichiers Créés:
1. `auth-service/src/main/java/com/uasz/daos/auth/controller/EnseignantProxyController.java`
2. `auth-service/src/main/java/com/uasz/daos/auth/config/RestTemplateConfig.java`
3. `auth-service/src/main/resources/templates/enseignant-list.html`
4. `auth-service/src/main/resources/templates/enseignant-add.html`
5. `auth-service/src/main/resources/templates/enseignant-edit.html`
6. `auth-service/src/main/resources/templates/enseignant-details.html`
7. `auth-service/src/main/resources/templates/enseignant-archive-list.html`
8. `enseignant-service/src/main/java/com/uasz/daos/enseignant/controller/EnseignantRestController.java`

### Fichiers Modifiés:
1. `auth-service/src/main/resources/templates/dashboard-enseignant.html` - Ajout des liens vers la gestion enseignant

## Étapes de Déploiement

1. **Compiler les services**:
   ```bash
   cd auth-service && mvn clean package
   cd ../enseignant-service && mvn clean package
   ```

2. **Démarrer Eureka Server**:
   ```bash
   cd eureka-server && mvn spring-boot:run
   ```

3. **Démarrer Enseignant-Service**:
   ```bash
   cd enseignant-service && mvn spring-boot:run
   ```

4. **Démarrer Auth-Service**:
   ```bash
   cd auth-service && mvn spring-boot:run
   ```

5. **Accéder à l'application**:
   - URL: `http://localhost:8081`
   - Dashboard enseignant: `http://localhost:8081/dashboard/enseignant`
   - Liste des enseignants: `http://localhost:8081/lst-enseignants`
