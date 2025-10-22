@echo off
echo ========================================
echo     Compilation MCLocalAuth Plugin
echo ========================================

REM Vérifier si Maven est installé
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Maven n'est pas installé ou pas dans le PATH
    echo.
    echo Pour installer Maven:
    echo 1. Téléchargez Maven depuis https://maven.apache.org/download.cgi
    echo 2. Décompressez l'archive
    echo 3. Ajoutez le dossier bin de Maven au PATH système
    echo 4. Redémarrez votre terminal
    echo.
    pause
    exit /b 1
)

echo Maven trouvé, compilation en cours...
echo.

REM Nettoyer et compiler
mvn clean compile package

if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================
    echo       COMPILATION RÉUSSIE !
    echo ========================================
    echo.
    echo Le plugin compilé se trouve dans:
    echo target\MCLocalAuth.jar
    echo.
    echo Vous pouvez maintenant:
    echo 1. Copier le JAR dans votre dossier plugins/
    echo 2. Mettre à jour votre config.yml
    echo 3. Redémarrer votre serveur Minecraft
    echo.
) else (
    echo.
    echo ========================================
    echo       ERREUR DE COMPILATION
    echo ========================================
    echo.
    echo Vérifiez les erreurs ci-dessus
    echo.
)

pause