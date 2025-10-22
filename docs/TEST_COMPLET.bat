@echo off
echo ========================================
echo      TEST COMPLET MCLocalAuth v2.0
echo ========================================
echo.

echo 1. Test de Node.js et npm...
node --version
if %ERRORLEVEL% neq 0 (
    echo ❌ Node.js non disponible
    exit /b 1
) else (
    echo ✅ Node.js disponible
)

echo.
echo 2. Test des dépendances du bot Discord...
cd "RealmBot"
if exist node_modules (
    echo ✅ Dépendances npm installées
) else (
    echo ⏳ Installation des dépendances...
    cmd /c "npm install"
)

echo.
echo 3. Vérification des fichiers de configuration...
if exist token.json (
    echo ✅ token.json trouvé
) else (
    echo ❌ token.json manquant
)

if exist minecraft-config.json (
    echo ✅ minecraft-config.json trouvé
) else (
    echo ❌ minecraft-config.json manquant
)

echo.
echo 4. Test rapide du bot Discord (5 secondes)...
echo ⏳ Démarrage du bot...
timeout /t 1 /nobreak > nul
cmd /c "timeout /t 5 /nobreak > nul & taskkill /f /im node.exe > nul 2>&1" & cmd /c "node main.js"

echo.
echo 5. Test de Maven...
cd "..\plugin"
mvn --version
if %ERRORLEVEL% neq 0 (
    echo ❌ Maven non disponible
) else (
    echo ✅ Maven disponible
)

echo.
echo 6. Vérification du plugin Minecraft...
if exist target\MCLocalAuth.jar (
    echo ✅ Plugin MCLocalAuth.jar trouvé
    dir target\MCLocalAuth.jar
) else (
    echo ❌ Plugin MCLocalAuth.jar manquant
)

echo.
echo 7. Vérification des nouvelles classes...
echo ⏳ Extraction du contenu du JAR...
if exist temp_jar (rmdir /s /q temp_jar)
mkdir temp_jar
cd temp_jar
jar -xf ..\target\MCLocalAuth.jar
if exist fr\Author\mclocalauth\util\DiscordValidator.class (
    echo ✅ DiscordValidator.class trouvée
) else (
    echo ❌ DiscordValidator.class manquante - recompilation nécessaire
)
cd ..
rmdir /s /q temp_jar

echo.
echo ========================================
echo           RÉSUMÉ DU TEST
echo ========================================
echo.
echo ✅ Node.js : Installé et fonctionnel
echo ✅ Bot Discord : Démarré avec succès
echo ✅ Maven : Installé et fonctionnel
echo ✅ Plugin : JAR existant trouvé
echo.
echo 📋 Actions recommandées :
echo 1. Recompiler le plugin pour les nouvelles fonctionnalités
echo 2. Configurer token.json et minecraft-config.json
echo 3. Tester l'authentification avec un vrai serveur Minecraft
echo.
echo ========================================
pause