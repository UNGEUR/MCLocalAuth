@echo off
chcp 65001 >nul
echo ========================================
echo  Anonymisation du projet MCLocalAuth
echo ========================================
echo.

echo Remplacement de "Author" par "Author"...
echo Remplacement de "Dev" par "Dev"...
echo Remplacement du package "fr.Author" par "fr.mclocalauth"...
echo.

powershell -Command "& {Get-ChildItem -Recurse -Include *.java,*.xml,*.yml,*.md,*.bat,*.json -File | ForEach-Object { $content = Get-Content $_.FullName -Raw -Encoding UTF8; $content = $content -replace 'Author', 'Author'; $content = $content -replace 'Dev', 'Dev'; $content = $content -replace 'fr\.Author', 'fr.mclocalauth'; $content = $content -replace 'fr/Author', 'fr/mclocalauth'; [System.IO.File]::WriteAllText($_.FullName, $content, [System.Text.Encoding]::UTF8) }}"

echo.
echo Renommage des dossiers...

if exist "plugin\src\main\java\fr\Author" (
    echo Renommage: plugin\src\main\java\fr\Author -^> plugin\src\main\java\fr\mclocalauth
    move "plugin\src\main\java\fr\Author" "plugin\src\main\java\fr\mclocalauth_temp" >nul
    move "plugin\src\main\java\fr\mclocalauth_temp" "plugin\src\main\java\fr\mclocalauth" >nul
)

if exist "bungeecord\src\main\java\fr\Author" (
    echo Renommage: bungeecord\src\main\java\fr\Author -^> bungeecord\src\main\java\fr\mclocalauth
    move "bungeecord\src\main\java\fr\Author" "bungeecord\src\main\java\fr\mclocalauth_temp" >nul
    move "bungeecord\src\main\java\fr\mclocalauth_temp" "bungeecord\src\main\java\fr\mclocalauth" >nul
)

echo.
echo ✅ Anonymisation terminée !
echo.
echo Les occurrences suivantes ont été remplacées:
echo   - "Author" -^> "Author"
echo   - "Dev" -^> "Dev"
echo   - "fr.Author" -^> "fr.mclocalauth"
echo.
pause
