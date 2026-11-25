@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac -cp ".;lib\sqlite-jdbc-3.54.1.0.jar" -d out src\ravline\*.java
if errorlevel 1 exit /b 1
java --enable-native-access=ALL-UNNAMED -cp "out;lib\sqlite-jdbc-3.54.1.0.jar" ravline.Main
