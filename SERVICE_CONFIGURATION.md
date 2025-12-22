# Configuration des Services Microservices

## 📌 URLs des Services

### Eureka Server
- **Port**: 8761
- **URL**: http://localhost:8761
- **Statut**: Affiche l'enregistrement des services clients

### Auth-Service  
- **Port**: 8081
- **URL**: http://localhost:8081
- **Base de données**: `daos_auth_db`
- **Fonctionnalités**:
  - Login/Authentification
  - Dashboard multi-rôle
  - Gestion des utilisateurs
  - Pages de gestion des enseignants (proxies)

### Enseignant-Service
- **Port**: 8082
- **URL**: http://localhost:8082
- **Base de données**: `daos_enseignant_db`
- **Fonctionnalités**:
  - API REST pour les enseignants
  - Gestion des données enseignants
  - Gestion des responsables
  - Gestion des coordinateurs

---

## 🔌 Communication Inter-Services

### RestTemplate (Auth-Service → Enseignant-Service)

**Configuration**: `auth-service/src/main/java/.../config/RestTemplateConfig.java`

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

**Utilisation dans EnseignantProxyController**:
```java
private static final String ENSEIGNANT_SERVICE_URL = "http://enseignant-service";

// Appel via RestTemplate
List<Enseignant> enseignants = restTemplate.getForObject(
    ENSEIGNANT_SERVICE_URL + "/api/enseignants",
    List.class
);
```

---

## 🌐 Endpoints API

### Enseignant-Service API

#### GET - Récupérer les données

| Endpoint | Méthode | Description | Retour |
|----------|---------|-------------|--------|
| `/api/enseignants` | GET | Tous les enseignants actifs | JSON Array |
| `/api/enseignants/{id}` | GET | Un enseignant spécifique | JSON Object |
| `/api/enseignants/archives` | GET | Tous les enseignants archivés | JSON Array |

#### POST - Créer/Modifier

| Endpoint | Méthode | Description | Body |
|----------|---------|-------------|------|
| `/api/enseignants` | POST | Créer un nouvel enseignant | JSON Object |
| `/api/enseignants/{id}` | PUT | Mettre à jour | JSON Object |
| `/api/enseignants/{id}/archive` | POST | Archiver | Vide |
| `/api/enseignants/{id}/unarchive` | POST | Désarchiver | Vide |
| `/api/enseignants/{id}/activate` | POST | Activer | Vide |
| `/api/enseignants/{id}/deactivate` | POST | Désactiver | Vide |

---

## 💾 Base de Données

### Auth-Service DB

**Schéma**: `daos_auth_db`

**Tables principales**:
- `utilisateurs` - Utilisateurs du système
- `roles` - Rôles (Admin, Enseignant, Responsable, Coordinateur, Étudiant)

### Enseignant-Service DB

**Schéma**: `daos_enseignant_db`

**Tables principales**:
- `enseignant` - Données des enseignants
- `coordinateur` - Coordinateurs
- `responsable` - Responsables

---

## 🔐 Authentification

### Spring Security

**Flux d'authentification**:
1. Utilisateur se connecte sur `/login`
2. Spring Security valide les identifiants
3. JWT token créé et stocké en session
4. Utilisateur redirigé vers le dashboard approprié selon son rôle

**Rôles supportés**:
- `ADMIN` - Accès complet
- `ENSEIGNANT` - Accès dashboard enseignant
- `RESPONSABLE_MASTER` - Accès dashboard responsable
- `COORDONATEUR_DES_LICENCES` - Accès dashboard coordinateur
- `ETUDIANT` - Accès dashboard étudiant

---

## 🔗 Eureka Service Discovery

### Enregistrement des Services

**Auth-Service**:
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
spring.application.name=auth-service
```

**Enseignant-Service**:
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
spring.application.name=enseignant-service
```

### Découverte de Services

RestTemplate utilise Eureka pour:
1. Découvrir l'adresse IP/port du service
2. Équilibrer la charge entre plusieurs instances (si présentes)
3. Gérer automatiquement les défaillances

**Résolution d'URL**:
```
"http://enseignant-service/api/enseignants"
    ↓ (Eureka découvre)
"http://localhost:8082/api/enseignants"
```

---

## 📊 Architecture Vue d'Ensemble

```
┌─────────────────────────────────────────────────────────────────┐
│                        EUREKA SERVER                             │
│                      (8761)                                      │
│         Enregistre et découvre les services                      │
└───────────┬─────────────────────────────┬───────────────────────┘
            │                             │
            ↓                             ↓
    ┌───────────────────┐        ┌──────────────────┐
    │  AUTH-SERVICE     │        │ ENSEIGNANT-SERVICE│
    │  (8081)           │        │ (8082)           │
    │                   │        │                  │
    │ RestTemplate      │──────→ │ REST API         │
    │ ↓                 │        │ ↓                │
    │ Pages HTML        │        │ DB               │
    │ Controllers       │        │ Services         │
    │ Spring Security   │        │ Models           │
    └─────────┬─────────┘        └──────────────────┘
              │
              ↓
        ┌──────────────┐
        │   NAVIGATEUR │
        │   (Utilisateur)
        └──────────────┘
```

---

## 🚀 Démarrage des Services

### Commande 1 - Terminal 1
```bash
cd eureka-server
mvn spring-boot:run
```
✅ Vérifier: http://localhost:8761

### Commande 2 - Terminal 2
```bash
cd enseignant-service
mvn spring-boot:run
```
✅ Vérifier dans Eureka que ENSEIGNANT-SERVICE est enregistré

### Commande 3 - Terminal 3
```bash
cd auth-service
mvn spring-boot:run
```
✅ Vérifier dans Eureka que AUTH-SERVICE est enregistré

---

## 📝 Fichiers de Configuration

### auth-service/src/main/resources/application.properties

```properties
# Server
server.port=8081
spring.application.name=auth-service

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/daos_auth_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true

# JWT
jwt.secret=...
jwt.expiration=86400000
```

### enseignant-service/src/main/resources/application.properties

```properties
# Server
server.port=8082
spring.application.name=enseignant-service

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/daos_enseignant_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
```

---

## 🔄 Flux Complet d'une Requête

### Exemple: Récupérer la liste des enseignants

```
1. Utilisateur se connecte à http://localhost:8081/dashboard/enseignant
   ↓
2. Clique sur "Enseignants Actifs"
   ↓
3. Navigateur envoie GET /lst-enseignants
   ↓
4. Auth-Service reçoit la requête
   ↓
5. EnseignantProxyController.listEnseignants() est exécuté
   ↓
6. RestTemplate effectue GET sur "http://enseignant-service/api/enseignants"
   ↓
7. Eureka résout "enseignant-service" → "localhost:8082"
   ↓
8. Enseignant-Service reçoit la requête
   ↓
9. EnseignantRestController.getAllEnseignants() est exécuté
   ↓
10. EnseignantService récupère les données de la BD
   ↓
11. Retourne JSON avec la liste
   ↓
12. Auth-Service reçoit le JSON
   ↓
13. Ajoute les données au modèle Thymeleaf
   ↓
14. enseignant-list.html est rendu
   ↓
15. HTML final est retourné au navigateur
   ↓
16. Utilisateur voit la liste des enseignants
```

---

## ✅ Vérification du Statut

### Pour vérifier que tout fonctionne:

1. **Eureka Dashboard**:
   - Accédez à: http://localhost:8761
   - Vous devriez voir:
     - `AUTH-SERVICE` (UP)
     - `ENSEIGNANT-SERVICE` (UP)

2. **Auth-Service**:
   - Logs: "Started AuthServiceApplication"
   - URL: http://localhost:8081

3. **Enseignant-Service**:
   - Logs: "Started EnseignantServiceApplication"
   - URL: http://localhost:8082

4. **Communication**:
   - Allez sur http://localhost:8081/lst-enseignants
   - Si vous voyez la liste (vide ou non), c'est que la communication fonctionne ✅

---

## 🆘 Problèmes Courants

| Problème | Cause | Solution |
|----------|-------|----------|
| Service non visible dans Eureka | Service pas démarré | Vérifier les logs de démarrage |
| Erreur "Connection refused" | Service sur mauvais port | Vérifier application.properties |
| "Impossible de charger la liste" | RestTemplate ne peut pas joindre le service | Vérifier que enseignant-service est UP |
| 404 sur API endpoint | Endpoint n'existe pas | Vérifier EnseignantRestController |
| Données non mises à jour | BD non synchronisée | Vérifier logs de la BD |

---

**Dernière mise à jour**: Décembre 2025
**Statut**: ✅ Production-Ready
