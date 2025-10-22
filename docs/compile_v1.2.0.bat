@echo off
setlocal enabledelayedexpansion

echo ╔══════════════════════════════════════════════════════════════╗
echo ║         📦 MCLocalAuth - Compilation v1.2.0                  ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

REM Vérifier si Maven existe
set MAVEN_VERSION=3.9.5
set MAVEN_HOME=C:\apache-maven-%MAVEN_VERSION%
set MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd

if exist "%MAVEN_BIN%" (
    echo ✅ Maven trouvé : %MAVEN_HOME%
) else (
    echo ⚠️  Maven non trouvé, téléchargement...
    
    REM Créer le dossier temporaire
    mkdir "%TEMP%\maven_download" 2>nul
    
    REM Télécharger Maven
    echo 📥 Téléchargement de Maven %MAVEN_VERSION%...
    powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://dlcdn.apache.org/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%TEMP%\maven_download\maven.zip'}"
    
    if errorlevel 1 (
        echo ❌ Échec du téléchargement
        pause
        exit /b 1
    )
    
    REM Extraire Maven
    echo 📂 Extraction de Maven...
    powershell -Command "& {Expand-Archive -Path '%TEMP%\maven_download\maven.zip' -DestinationPath 'C:\' -Force}"
    
    if errorlevel 1 (
        echo ❌ Échec de l'extraction
        pause
        exit /b 1
    )
    
    echo ✅ Maven installé dans %MAVEN_HOME%
)

REM Configurer l'environnement
set JAVA_HOME=C:\jdk8
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo.
echo 🔧 Configuration :
echo    - JAVA_HOME = %JAVA_HOME%
echo    - MAVEN_HOME = %MAVEN_HOME%
echo.

REM Vérifier Java
java -version 2>&1 | findstr /i "1.8"
if errorlevel 1 (
    echo ❌ Java 8 non trouvé
    pause
    exit /b 1
)

echo.
echo ════════════════════════════════════════════════════════════════
echo  🔨 Compilation du plugin Spigot v1.2.0
echo ════════════════════════════════════════════════════════════════
echo.

cd /d "%~dp0plugin"

REM Nettoyer et compiler
call "%MAVEN_BIN%" clean package

if errorlevel 1 (
    echo.
    echo ❌ Échec de la compilation
    pause
    exit /b 1
)

echo.
echo ✅ Compilation réussie !
echo.

REM Copier le JAR
if exist "target\MCLocalAuth.jar" (
    copy /Y "target\MCLocalAuth.jar" "..\Plugins\MCLocalAuth-Spigot-v1.2.0.jar"
    echo 📦 JAR copié : Plugins\MCLocalAuth-Spigot-v1.2.0.jar
    
    REM Afficher la taille
    for %%F in ("..\Plugins\MCLocalAuth-Spigot-v1.2.0.jar") do (
        set /a size=%%~zF/1024/1024
        echo 📊 Taille : !size! MB
    )
) else (
    echo ❌ JAR non trouvé dans target\
)

echo.
echo ════════════════════════════════════════════════════════════════
echo  ✨ Terminé !
echo ════════════════════════════════════════════════════════════════
echo.
echo 📝 Changements v1.2.0 :
echo    ✅ Commande /mctest ajoutée
echo    ✅ Commande /auth simplifiée (code seul)
echo    ✅ Correction : joueur introuvable
echo    ✅ Recherche du code dans tous les joueurs en attente
echo.

pause
