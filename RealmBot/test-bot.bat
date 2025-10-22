@echo off
echo ========================================
echo        Test du Bot Discord RealmBot
echo ========================================

REM Vérifier si Node.js est installé
where node >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo ERREUR: Node.js n'est pas installé ou pas dans le PATH
    echo.
    echo Pour installer Node.js:
    echo 1. Téléchargez Node.js depuis https://nodejs.org/
    echo 2. Installez la version LTS
    echo 3. Redémarrez votre terminal
    echo 4. Relancez ce script
    echo.
    pause
    exit /b 1
)

echo Node.js trouvé, vérification des dépendances...
echo.

REM Vérifier si les dépendances sont installées
if not exist node_modules (
    echo Installation des dépendances npm...
    npm install
    echo.
)

REM Vérifier la configuration
if not exist token.json (
    echo ATTENTION: Le fichier token.json n'existe pas !
    echo Créez le fichier token.json avec votre configuration Discord.
    echo.
    pause
    exit /b 1
)

if not exist minecraft-config.json (
    echo ATTENTION: Le fichier minecraft-config.json n'existe pas !
    echo Créez le fichier minecraft-config.json avec la configuration du serveur.
    echo.
    pause
    exit /b 1
)

echo Configuration trouvée, démarrage du bot...
echo.
echo ========================================
echo           BOT DISCORD DÉMARRÉ
echo ========================================
echo.
echo Pour arrêter le bot, appuyez sur Ctrl+C
echo.

node main.js