# Projet DAOS 2024-2025 - Architecture Microservices

## Gestion des Enseignements à l'UASZ

Application orientée services (Spring Boot, REST, Spring Cloud et MySQL) pour gérer les enseignements des PER et des Vacataires de la licence au Master.

## Architecture

L'application est composée de **9 services** organisés en architecture microservices :

### Infrastructure Spring Cloud
1. **Eureka Server** (Port 8761) - Service Registry
2. **Config Server** (Port 8888) - Serveur de configuration centralisée
3. **API Gateway** (Port 8080) - Point d'entrée unique pour tous les microservices

### Microservices Métier
4. **Auth Service** (Port 8081) - Authentification et autorisation
5. **Enseignant Service** (Port 8082) - Gestion des enseignants et vacataires
6. **Maquette Service** (Port 8083) - Gestion des formations, UE, EC, maquettes
7. **Choix Enseignement Service** (Port 8084) - Gestion des choix d'enseignements
8. **Emploi Temps Service** (Port 8085) - Gestion de l'emploi du temps
9. **Deroulement Enseignement Service** (Port 8086) - Suivi du déroulement des cours, barres de progression, statistiques, génération PDF

## Technologies

- Java 17
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- MySQL 8.0
- Docker & Docker Compose
- Maven
- Eureka (Service Discovery)
- Spring Cloud Gateway
- Spring Cloud Config
- OpenFeign (Communication inter-services)
- Spring Data JPA
- JWT (Authentification)
- iText (Génération PDF)

## Structure du Projet

```
microservices-daos/
├── eureka-server/               # Service Registry
├── config-server/               # Configuration Server
├── api-gateway/                 # API Gateway
├── auth-service/                # Service d'authentification
├── enseignant-service/          # Service gestion enseignants
├── maquette-service/            # Service gestion maquettes
├── choix-enseignement-service/  # Service choix enseignements
├── emploi-temps-service/        # Service emploi du temps
├── deroulement-enseignement-service/ # Service déroulement
├── docker-compose.yml           # Orchestration Docker
└── README.md                    # Documentation
```

## Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- Docker et Docker Compose
- MySQL 8.0 (ou utiliser le conteneur Docker fourni)

## Installation et Démarrage

### Option 1 : Avec Docker Compose (Recommandé)

1. Cloner le projet
```bash
cd microservices-daos
```

2. Construire et démarrer tous les services
```bash
docker-compose up --build
```

3. Vérifier que tous les services sont lancés
```bash
docker-compose ps
```

### Option 2 : Exécution locale (sans Docker)

1. Démarrer MySQL localement
```bash
# Assurez-vous que MySQL est installé et en cours d'exécution
# Utilisateur: root
# Mot de passe: root
```

2. Démarrer les services dans l'ordre suivant :

```bash
# 1. Eureka Server
cd eureka-server
mvn spring-boot:run

# 2. Config Server (dans un nouveau terminal)
cd config-server
mvn spring-boot:run

# 3. API Gateway (dans un nouveau terminal)
cd api-gateway
mvn spring-boot:run

# 4-9. Microservices métier (dans de nouveaux terminaux)
cd auth-service
mvn spring-boot:run

cd enseignant-service
mvn spring-boot:run

cd maquette-service
mvn spring-boot:run

cd choix-enseignement-service
mvn spring-boot:run

cd emploi-temps-service
mvn spring-boot:run

cd deroulement-enseignement-service
mvn spring-boot:run
```

## Accès aux Services

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Tableau de bord des services enregistrés |
| API Gateway | http://localhost:8080 | Point d'entrée de l'API |
| Config Server | http://localhost:8888 | Serveur de configuration |
| Auth Service | http://localhost:8081 | Service d'authentification |
| Enseignant Service | http://localhost:8082 | Gestion des enseignants |
| Maquette Service | http://localhost:8083 | Gestion des maquettes |
| Choix Enseignement Service | http://localhost:8084 | Gestion des choix |
| Emploi Temps Service | http://localhost:8085 | Gestion emploi du temps |
| Deroulement Enseignement Service | http://localhost:8086 | Suivi déroulement |

## Routes API Gateway

Toutes les requêtes passent par l'API Gateway (port 8080) :

- `http://localhost:8080/api/auth/**` → Auth Service
- `http://localhost:8080/api/enseignants/**` → Enseignant Service
- `http://localhost:8080/api/maquettes/**` → Maquette Service
- `http://localhost:8080/api/choix-enseignements/**` → Choix Enseignement Service
- `http://localhost:8080/api/emploi-temps/**` → Emploi Temps Service
- `http://localhost:8080/api/deroulement-enseignements/**` → Deroulement Enseignement Service

## Base de Données

Chaque microservice possède sa propre base de données MySQL :

- `daos_auth_db` - Utilisateurs et authentification
- `daos_enseignant_db` - Enseignants et vacataires
- `daos_maquette_db` - Formations, UE, EC, maquettes
- `daos_choix_enseignement_db` - Choix d'enseignements
- `daos_emploi_temps_db` - Planning et emplois du temps
- `daos_deroulement_enseignement_db` - Suivi des cours

## Développement

### Compiler un service spécifique
```bash
cd <nom-du-service>
mvn clean package
```

### Lancer les tests
```bash
mvn test
```

### Générer le JAR
```bash
mvn clean package -DskipTests
```

## Fonctionnalités par Microservice

### 1. Auth Service
- Authentification JWT
- Gestion des utilisateurs
- Autorisation et rôles

### 2. Enseignant Service
- CRUD Enseignants
- Gestion des PER et Vacataires
- Informations : nom, prénom, grade, type

### 3. Maquette Service
- Gestion des formations
- Gestion des classes
- Gestion des maquettes pédagogiques
- Gestion des UE (Unités d'Enseignement)
- Gestion des EC (Éléments Constitutifs)

### 4. Choix Enseignement Service
- Gestion des choix d'enseignements des enseignants
- Affectation des cours aux enseignants

### 5. Emploi Temps Service
- Création et gestion des emplois du temps
- Planification par semaine/semestre
- Affectation enseignants ↔ enseignements

### 6. Deroulement Enseignement Service
- Suivi du déroulement des cours
- Barres de progression (% heures effectuées)
- Génération de rapports PDF
- Statistiques et tableaux de bord

## Méthodologie Agile

Le projet suit la méthodologie Scrum avec :
- Product Owner
- Scrum Master
- Équipe de développement (7-8 membres)
- Équipe de test et validation (3 membres)

### Sprints
- Sprint Planning : 1 heure
- Sprint : 1 semaine
- Daily Stand-up : 15 minutes
- Sprint Review : 1 heure
- Sprint Retrospective : 30 minutes

## Livrables

1. Product Backlog
2. Sprint Backlog (Kanban)
3. Dossier d'analyse et conception (UML)
4. Tests unitaires et d'intégration
5. Répertoire Git avec pull requests
6. Pipeline CI/CD
7. Démo fonctionnelle
8. Rapport de rétrospective

## Date limite

**15 Novembre 2025 à 23h**

## Démo

**Après le 20 Novembre 2025**

## Commandes Utiles

### Docker
```bash
# Construire et démarrer tous les services
docker-compose up --build

# Démarrer en mode détaché
docker-compose up -d

# Arrêter tous les services
docker-compose down

# Voir les logs d'un service
docker-compose logs -f <nom-service>

# Reconstruire un service spécifique
docker-compose build <nom-service>

# Redémarrer un service
docker-compose restart <nom-service>
```

### Maven
```bash
# Nettoyer et compiler
mvn clean install

# Lancer l'application
mvn spring-boot:run

# Créer un package sans tests
mvn clean package -DskipTests
```

## Troubleshooting

### Problème : Port déjà utilisé
```bash
# Windows
netstat -ano | findstr :<PORT>
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :<PORT>
kill -9 <PID>
```

### Problème : Connexion MySQL refusée
- Vérifier que MySQL est démarré
- Vérifier les credentials (root/root)
- Vérifier que le port 3306 n'est pas bloqué

### Problème : Services ne s'enregistrent pas dans Eureka
- Attendre 30 secondes après le démarrage d'Eureka
- Vérifier la configuration `eureka.client.serviceUrl.defaultZone`
- Consulter les logs du service

## Auteurs

Projet réalisé dans le cadre du cours :
- **Génie Logiciel Avancé & Développement d'Applications Orientées Services**
- **Licence 3 Ingénierie Informatique**
- **Université Assane Seck de Ziguinchor**
- **Année universitaire 2024/2025**

Encadré par : **Pr Ibrahima DIOP**

## License

Ce projet est développé dans un cadre académique.
