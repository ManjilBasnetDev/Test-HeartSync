#!/bin/bash

# Ensure the script exits on any error
set -e

# Define variables
APP_NAME="HeartSync"
APP_VERSION="1.0"
MAIN_JAR="target/heartsync-1.0-SNAPSHOT-jar-with-dependencies.jar"
ICON_FILE="src/ImagePicker/HomePageCoupleImg.png"

# First ensure we have a fresh build
mvn clean package

# Create the Windows executable
jpackage \
  --name $APP_NAME \
  --app-version $APP_VERSION \
  --input target \
  --main-jar $(basename $MAIN_JAR) \
  --main-class heartsync.HeartSync \
  --type exe \
  --icon $ICON_FILE \
  --win-dir-chooser \
  --win-shortcut \
  --win-menu \
  --win-menu-group "HeartSync" \
  --vendor "HeartSync Team" \
  --copyright "Copyright © 2024 HeartSync Team" \
  --description "HeartSync Dating Application" \
  --win-per-user-install 