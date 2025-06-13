@echo off

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib

REM Download required dependencies
if not exist lib\sqlite-jdbc-3.45.1.0.jar (
    echo Downloading SQLite JDBC driver...
    curl -L https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar -o lib\sqlite-jdbc-3.45.1.0.jar
)

if not exist lib\flatlaf-3.4.jar (
    echo Downloading FlatLaf...
    curl -L https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4/flatlaf-3.4.jar -o lib\flatlaf-3.4.jar
)

if not exist lib\flatlaf-intellij-themes-3.4.jar (
    echo Downloading FlatLaf IntelliJ Themes...
    curl -L https://repo1.maven.org/maven2/com/formdev/flatlaf-intellij-themes/3.4/flatlaf-intellij-themes-3.4.jar -o lib\flatlaf-intellij-themes-3.4.jar
)

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Find all Java files
echo Finding Java files...
dir /s /b src\*.java > sources.txt

REM Compile the project
echo Compiling project...
javac -d bin -cp "lib\*" @sources.txt

REM Clean up
del sources.txt

REM Run the application
echo Running application...
java -cp "bin;lib\*" heartsync.HeartSyncApp 