@echo off
chcp 65001 >nul
echo ========================================
echo  MCLocalAuth - Bot Discord Intégré
echo  Script de Compilation
echo ========================================
echo.

cd plugin

echo [1/3] Nettoyage des anciens builds...
call mvn clean
if %errorlevel% neq 0 (
    echo ❌ Erreur lors du nettoyage
    pause
    exit /b 1
)

echo.
echo [2/3] Compilation du plugin...
call mvn package
if %errorlevel% neq 0 (
    echo ❌ Erreur lors de la compilation
    pause
    exit /b 1
)

echo.
echo [3/3] Vérification du JAR...
if exist "target\MCLocalAuth.jar" (
    echo ✅ Compilation réussie !
    echo.
    echo 📦 Le fichier est disponible ici :
    echo    %cd%\target\MCLocalAuth.jar
    echo.
    echo 📋 Prochaines étapes :
    echo    1. Copiez MCLocalAuth.jar dans plugins/ de votre serveur
    echo    2. Configurez config.yml avec votre token Discord
    echo    3. Redémarrez le serveur Minecraft
    echo.
) else (
    echo ❌ Le fichier JAR n'a pas été trouvé
    pause
    exit /b 1
)

cd ..
pause
