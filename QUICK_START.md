# ⚡ Quick Start - Pages Enseignant dans Auth-Service

## 🚀 Démarrage Rapide (5 minutes)

### Étape 1: Compiler les Services (2 min)

```bash
# Terminal à la racine du projet
mvn clean package -DskipTests
```

### Étape 2: Démarrer Eureka Server (Terminal 1)

```bash
cd eureka-server
mvn spring-boot:run
```

✅ **Vérifier**: http://localhost:8761 (vous voyez la page Eureka)

### Étape 3: Démarrer Enseignant-Service (Terminal 2)

```bash
cd enseignant-service
mvn spring-boot:run
```

✅ **Vérifier**: Logs affichent "Started EnseignantServiceApplication"  
✅ **Dans Eureka**: ENSEIGNANT-SERVICE (UP) visible à http://localhost:8761

### Étape 4: Démarrer Auth-Service (Terminal 3)

```bash
cd auth-service
mvn spring-boot:run
```

✅ **Vérifier**: Logs affichent "Started AuthServiceApplication"  
✅ **Dans Eureka**: AUTH-SERVICE (UP) visible à http://localhost:8761

### Étape 5: Accéder à l'Application

Ouvrez votre navigateur:

1. **Dashboard Enseignant**:
   ```
   http://localhost:8081/dashboard/enseignant
   ```

2. **Liste des Enseignants**:
   ```
   http://localhost:8081/lst-enseignants
   ```

---

## ✅ Test Rapide (1 minute)

### Sur la page `/lst-enseignants`:

1. ✅ Voyez-vous "Liste des Enseignants" ?
2. ✅ Y a-t-il un bouton "Nouvel Enseignant" ?
3. ✅ Y a-t-il une barre de recherche ?
4. ✅ Y a-t-il un bouton "Accéder aux Archives" ?

**Si tout ✅ → L'intégration fonctionne!**

---

## 🎯 Principales Fonctionnalités

| Fonctionnalité | URL | Action |
|---|---|---|
| **Liste** | `/lst-enseignants` | Voir tous les enseignants |
| **Ajouter** | `/add-enseignant` | Créer un nouvel enseignant |
| **Modifier** | `/edit-enseignant/{id}` | Éditer un enseignant |
| **Détails** | `/view-enseignant/{id}` | Voir le dossier complet |
| **Archives** | `/lst-enseignants-archives` | Voir les archivés |

---

## 📚 Documentation Complète

Pour plus d'informations:

```
IMPLEMENTATION_COMPLETE.md    ← Résumé complet
INTEGRATION_ENSEIGNANT_PAGES.md ← Architecture détaillée
IMPLEMENTATION_SUMMARY.md     ← Vue d'ensemble
TEST_GUIDE.md                 ← Guide de test complet
SERVICE_CONFIGURATION.md      ← Configuration technique
```

---

## 🆘 Si ça ne fonctionne pas

### Erreur: "Impossible de charger la liste"

```bash
# Vérifiez que enseignant-service est UP
# Allez à http://localhost:8761
# Cherchez ENSEIGNANT-SERVICE
# Si absent → Redémarrer le service
```

### Erreur: Services pas enregistrés dans Eureka

```bash
# Vérifiez les logs de chaque service
# Cherchez "Registered with Eureka server"
# Si absent → Vérifier application.properties
```

### Erreur: Port déjà utilisé

```bash
# Windows: netstat -ano | findstr :8081
# Linux: lsof -i :8081
# Terminer le processus et recommencer
```

---

## 📊 Architecture Minimale

```
Eureka (8761)
    ↑
    ├─ Enseignant-Service (8082) + API REST
    └─ Auth-Service (8081) + Pages HTML
                ↓
             Browser
```

---

## ⏱️ Temps d'Attente

- **Compilation**: 1-2 min
- **Eureka démarrage**: 10-15 sec
- **Enseignant-Service**: 20-30 sec
- **Auth-Service**: 20-30 sec
- **TOTAL**: ~2-3 min

---

## 🎉 C'est Prêt!

Une fois tous les services verts dans Eureka, vous pouvez:

```
✅ Voir la liste des enseignants
✅ Ajouter des enseignants
✅ Modifier des enseignants
✅ Archiver/Désarchiver
✅ Activer/Désactiver
✅ Voir les détails
```

---

**Vous avez besoin d'aide?** → Consultez les documents de documentation
