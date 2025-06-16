#!/bin/bash

# Create lib directory if it doesn't exist
mkdir -p lib

# Download required dependencies
if [ ! -f lib/sqlite-jdbc-3.45.1.0.jar ]; then
    echo "Downloading SQLite JDBC driver..."
    curl -L https://github.com/xerial/sqlite-jdbc/releases/download/3.45.1.0/sqlite-jdbc-3.45.1.0.jar -o lib/sqlite-jdbc-3.45.1.0.jar
fi

if [ ! -f lib/flatlaf-3.4.jar ]; then
    echo "Downloading FlatLaf..."
    curl -L https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4/flatlaf-3.4.jar -o lib/flatlaf-3.4.jar
fi

if [ ! -f lib/flatlaf-intellij-themes-3.4.jar ]; then
    echo "Downloading FlatLaf IntelliJ Themes..."
    curl -L https://repo1.maven.org/maven2/com/formdev/flatlaf-intellij-themes/3.4/flatlaf-intellij-themes-3.4.jar -o lib/flatlaf-intellij-themes-3.4.jar
fi

# Create bin directory if it doesn't exist
mkdir -p bin

# Find all Java files
echo "Finding Java files..."
find src -name "*.java" > sources.txt

# Compile the project
echo "Compiling project..."
javac -d bin -cp "lib/*" @sources.txt

# Clean up
rm sources.txt

# Run the application
echo "Running application..."
java -cp "bin:lib/*" heartsync.HeartSyncApp 