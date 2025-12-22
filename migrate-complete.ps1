# Script PowerShell pour Migrer TOUT le projet (Backend + Frontend)
# Usage: .\migrate-complete.ps1

$monolithPath = "C:\Users\Abdou\Documents\microservice"
$microservicesPath = "C:\Users\Abdou\Documents\microservices-daos"

Write-Host "=== Migration COMPLETE du Monolithe (Backend + Frontend) ===" -ForegroundColor Cyan
Write-Host ""

# Fonction pour copier un fichier
function Copy-FileIfExists {
    param(
        [string]$SourceFile,
        [string]$DestFile
    )

    if (Test-Path $SourceFile) {
        $destDir = Split-Path -Parent $DestFile
        if (!(Test-Path $destDir)) {
            New-Item -ItemType Directory -Force -Path $destDir | Out-Null
        }
        Copy-Item $SourceFile $DestFile -Force
        Write-Host "  OK Copie: $(Split-Path -Leaf $SourceFile)" -ForegroundColor Green
        return $true
    } else {
        Write-Host "  X  Non trouve: $(Split-Path -Leaf $SourceFile)" -ForegroundColor Yellow
        return $false
    }
}

# ===================================================================
# 1. COPIER LES FICHIERS STATIQUES (CSS, JS, Images) PARTOUT
# ===================================================================
Write-Host "`n[1/6] Copie des fichiers statiques (CSS, JS, Images)" -ForegroundColor Cyan

$services = @("enseignant-service", "maquette-service", "auth-service", "emploi-temps-service", "deroulement-enseignement-service")

foreach ($service in $services) {
    Write-Host "`nCopie des statiques vers $service..." -ForegroundColor Yellow

    # Copier tout le dossier static
    $staticSrc = "$monolithPath\src\main\resources\static"
    $staticDest = "$microservicesPath\$service\src\main\resources\static"

    if (Test-Path $staticSrc) {
        if (!(Test-Path $staticDest)) {
            New-Item -ItemType Directory -Force -Path $staticDest | Out-Null
        }
        Copy-Item -Path "$staticSrc\*" -Destination $staticDest -Recurse -Force
        Write-Host "  OK Fichiers statiques copies" -ForegroundColor Green
    }
}

# ===================================================================
# 2. MAQUETTE SERVICE - Templates HTML
# ===================================================================
Write-Host "`n[2/6] Migration des templates HTML: Maquette Service" -ForegroundColor Cyan

$maquetteDest = "$microservicesPath\maquette-service\src\main\resources\templates"
$templatesSrc = "$monolithPath\src\main\resources\templates"

# Templates pour maquette-service
$maquetteTemplates = @(
    "form-formation.html",
    "lst-formations.html",
    "lst-formations-archives.html",
    "form-filiere.html",
    "filiere-add.html",
    "lst-filieres.html",
    "form-niveau.html",
    "lst-niveaux.html",
    "module-add.html",
    "module-archived-list.html",
    "lst-modules.html",
    "ue-archived-list.html",
    "lst-ues.html",
    "ec-add.html",
    "ec-archived-list.html",
    "ec-list.html",
    "lst-ecs.html"
)

foreach ($template in $maquetteTemplates) {
    Copy-FileIfExists "$templatesSrc\$template" "$maquetteDest\$template"
}

# ===================================================================
# 3. ENSEIGNANT SERVICE - Templates HTML
# ===================================================================
Write-Host "`n[3/6] Migration des templates HTML: Enseignant Service" -ForegroundColor Cyan

$enseignantDest = "$microservicesPath\enseignant-service\src\main\resources\templates"

# Templates pour enseignant-service
$enseignantTemplates = @(
    "enseignant-add.html",
    "enseignant-archive-list.html",
    "enseignant-details.html",
    "enseignant-edit.html",
    "enseignant-list.html",
    "coordinateur-add.html",
    "coordinateur-detail.html",
    "coordinateur-edit.html",
    "coordinateur-list.html",
    "responsable-add.html",
    "responsable-coordinateur-add.html",
    "responsable-coordinateur-list.html",
    "responsable-detail.html",
    "responsable-edit.html",
    "responsable-list.html",
    "dashboard-enseignant.html",
    "dashboard-coordinateur.html",
    "dashboard-responsable.html"
)

foreach ($template in $enseignantTemplates) {
    Copy-FileIfExists "$templatesSrc\$template" "$enseignantDest\$template"
}

# ===================================================================
# 4. AUTH SERVICE - Templates HTML
# ===================================================================
Write-Host "`n[4/6] Migration des templates HTML: Auth Service" -ForegroundColor Cyan

$authDest = "$microservicesPath\auth-service\src\main\resources\templates"

# Templates pour auth-service
$authTemplates = @(
    "login.html",
    "select-role.html",
    "utilisateur-archive-list.html",
    "utilisateur-list.html",
    "index.html",
    "welcome.html"
)

foreach ($template in $authTemplates) {
    Copy-FileIfExists "$templatesSrc\$template" "$authDest\$template"
}

# ===================================================================
# 5. EMPLOI TEMPS SERVICE - Templates HTML
# ===================================================================
Write-Host "`n[5/6] Migration des templates HTML: Emploi Temps Service" -ForegroundColor Cyan

$emploiDest = "$microservicesPath\emploi-temps-service\src\main\resources\templates"

# Templates pour emploi-temps-service
$emploiTemplates = @(
    "planning-pdf-download.html",
    "planning-salle.html",
    "planning-salle-search.html",
    "salle-add-edit.html",
    "salle-list.html",
    "seance-add.html",
    "seance-delete-confirm.html",
    "seance-edit.html",
    "seance-enseignant-search.html",
    "seance-list.html"
)

foreach ($template in $emploiTemplates) {
    Copy-FileIfExists "$templatesSrc\$template" "$emploiDest\$template"
}

# ===================================================================
# 6. DEROULEMENT ENSEIGNEMENT SERVICE - Templates HTML
# ===================================================================
Write-Host "`n[6/6] Migration des templates HTML: Deroulement Service" -ForegroundColor Cyan

$deroulementDest = "$microservicesPath\deroulement-enseignement-service\src\main\resources\templates"

# Templates pour deroulement-service
$deroulementTemplates = @(
    "classe-add.html",
    "classe-detail.html",
    "classe-edit.html",
    "classe-list.html",
    "etudiant-add.html",
    "etudiant-list.html",
    "dashboard-etudiant.html",
    "note-cahier-add.html",
    "note-cahier-consultation.html",
    "note-cahier-edit.html",
    "note-cahier-historique.html",
    "note-cahier-list.html",
    "parametres.html"
)

foreach ($template in $deroulementTemplates) {
    Copy-FileIfExists "$templatesSrc\$template" "$deroulementDest\$template"
}

# ===================================================================
# 7. COPIER LES VIEW CONTROLLERS
# ===================================================================
Write-Host "`n[7/7] Copie des View Controllers" -ForegroundColor Cyan

# ViewController.java contient les mappings vers les templates
$viewControllerSrc = "$monolithPath\src\main\java\com\uasz\Atelier1_Gestion_Enseignements_UASZ\controller\ViewController.java"

foreach ($service in $services) {
    $viewControllerDest = "$microservicesPath\$service\src\main\java\com\uasz\daos\$(($service -replace '-service',''))\controller\ViewController.java"

    if (Test-Path $viewControllerSrc) {
        $destDir = Split-Path -Parent $viewControllerDest
        if (!(Test-Path $destDir)) {
            New-Item -ItemType Directory -Force -Path $destDir | Out-Null
        }

        # Copier et adapter le package
        $oldPkg = "com.uasz.Atelier1_Gestion_Enseignements_UASZ"
        $newPkg = "com.uasz.daos.$(($service -replace '-service',''))"

        (Get-Content $viewControllerSrc -Encoding UTF8) | ForEach-Object {
            $_ -replace [regex]::Escape($oldPkg), $newPkg
        } | Set-Content $viewControllerDest -Encoding UTF8

        Write-Host "  OK ViewController copie vers $service" -ForegroundColor Green
    }
}

# ===================================================================
# 8. COPIER LoginController, HomeController, GlobalController
# ===================================================================
Write-Host "`n[8/8] Copie des controllers supplementaires" -ForegroundColor Cyan

# LoginController vers auth-service
$loginSrc = "$monolithPath\src\main\java\com\uasz\Atelier1_Gestion_Enseignements_UASZ\controller\LoginController.java"
$loginDest = "$microservicesPath\auth-service\src\main\java\com\uasz\daos\auth\controller\LoginController.java"
if (Test-Path $loginSrc) {
    $destDir = Split-Path -Parent $loginDest
    if (!(Test-Path $destDir)) {
        New-Item -ItemType Directory -Force -Path $destDir | Out-Null
    }
    (Get-Content $loginSrc -Encoding UTF8) | ForEach-Object {
        $_ -replace "com.uasz.Atelier1_Gestion_Enseignements_UASZ", "com.uasz.daos.auth"
    } | Set-Content $loginDest -Encoding UTF8
    Write-Host "  OK LoginController copie" -ForegroundColor Green
}

# HomeController vers auth-service
$homeSrc = "$monolithPath\src\main\java\com\uasz\Atelier1_Gestion_Enseignements_UASZ\controller\HomeController.java"
$homeDest = "$microservicesPath\auth-service\src\main\java\com\uasz\daos\auth\controller\HomeController.java"
if (Test-Path $homeSrc) {
    (Get-Content $homeSrc -Encoding UTF8) | ForEach-Object {
        $_ -replace "com.uasz.Atelier1_Gestion_Enseignements_UASZ", "com.uasz.daos.auth"
    } | Set-Content $homeDest -Encoding UTF8
    Write-Host "  OK HomeController copie" -ForegroundColor Green
}

# GlobalController vers auth-service
$globalSrc = "$monolithPath\src\main\java\com\uasz\Atelier1_Gestion_Enseignements_UASZ\controller\GlobalController.java"
$globalDest = "$microservicesPath\auth-service\src\main\java\com\uasz\daos\auth\controller\GlobalController.java"
if (Test-Path $globalSrc) {
    (Get-Content $globalSrc -Encoding UTF8) | ForEach-Object {
        $_ -replace "com.uasz.Atelier1_Gestion_Enseignements_UASZ", "com.uasz.daos.auth"
    } | Set-Content $globalDest -Encoding UTF8
    Write-Host "  OK GlobalController copie" -ForegroundColor Green
}

Write-Host "`n=== Migration COMPLETE Terminee ===" -ForegroundColor Green
Write-Host ""
Write-Host "RESUME:" -ForegroundColor Cyan
Write-Host "  - Fichiers statiques (CSS/JS/Images) copies dans tous les services" -ForegroundColor White
Write-Host "  - Templates HTML repartis dans les microservices correspondants" -ForegroundColor White
Write-Host "  - ViewControllers copies dans chaque service" -ForegroundColor White
Write-Host ""
Write-Host "PROCHAINES ETAPES:" -ForegroundColor Yellow
Write-Host "  1. Ajouter les dependances Thymeleaf dans les pom.xml" -ForegroundColor White
Write-Host "  2. Adapter les controllers pour appeler les services locaux" -ForegroundColor White
Write-Host "  3. Tester chaque microservice avec son interface web" -ForegroundColor White
Write-Host ""
Write-Host "Voir MIGRATION_REPORT.md pour plus de details" -ForegroundColor Cyan
