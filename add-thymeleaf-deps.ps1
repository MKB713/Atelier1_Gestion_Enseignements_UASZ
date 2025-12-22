# Script pour ajouter les dependances Thymeleaf aux microservices
$microservicesPath = "C:\Users\Abdou\Documents\microservices-daos"

$services = @("maquette-service", "emploi-temps-service", "deroulement-enseignement-service")

$thymeleafDeps = @"
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity6</artifactId>
        </dependency>
"@

Write-Host "Ajout des dependances Thymeleaf..." -ForegroundColor Cyan

foreach ($service in $services) {
    $pomPath = "$microservicesPath\$service\pom.xml"

    if (Test-Path $pomPath) {
        $content = Get-Content $pomPath -Raw

        # Verifier si Thymeleaf est deja present
        if ($content -notmatch "spring-boot-starter-thymeleaf") {
            # Trouver la position apres validation et avant eureka-client
            $pattern = "(<dependency>\s+<groupId>org\.springframework\.boot</groupId>\s+<artifactId>spring-boot-starter-validation</artifactId>\s+</dependency>)"

            if ($content -match $pattern) {
                $newContent = $content -replace $pattern, "`$1`n$thymeleafDeps"
                Set-Content -Path $pomPath -Value $newContent -Encoding UTF8
                Write-Host "  OK $service - Thymeleaf ajoute" -ForegroundColor Green
            } else {
                Write-Host "  WARN $service - Pattern non trouve" -ForegroundColor Yellow
            }
        } else {
            Write-Host "  INFO $service - Thymeleaf deja present" -ForegroundColor Cyan
        }
    } else {
        Write-Host "  ERR $service - pom.xml non trouve" -ForegroundColor Red
    }
}

Write-Host "`nTermine!" -ForegroundColor Green
