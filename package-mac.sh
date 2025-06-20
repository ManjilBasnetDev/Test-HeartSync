#!/bin/bash

# Ensure the script exits on any error
set -e

# Define variables
APP_NAME="HeartSync"
APP_VERSION="1.0"
MAIN_JAR="target/heartsync-1.0-SNAPSHOT-jar-with-dependencies.jar"
ICON_FILE="src/ImagePicker/HomePageCoupleImg.png"

# Convert PNG to ICNS (required for Mac app icon)
mkdir -p AppIcon.iconset
sips -z 16 16   $ICON_FILE --out AppIcon.iconset/icon_16x16.png
sips -z 32 32   $ICON_FILE --out AppIcon.iconset/icon_16x16@2x.png
sips -z 32 32   $ICON_FILE --out AppIcon.iconset/icon_32x32.png
sips -z 64 64   $ICON_FILE --out AppIcon.iconset/icon_32x32@2x.png
sips -z 128 128 $ICON_FILE --out AppIcon.iconset/icon_128x128.png
sips -z 256 256 $ICON_FILE --out AppIcon.iconset/icon_128x128@2x.png
sips -z 256 256 $ICON_FILE --out AppIcon.iconset/icon_256x256.png
sips -z 512 512 $ICON_FILE --out AppIcon.iconset/icon_256x256@2x.png
sips -z 512 512 $ICON_FILE --out AppIcon.iconset/icon_512x512.png
sips -z 1024 1024 $ICON_FILE --out AppIcon.iconset/icon_512x512@2x.png
iconutil -c icns AppIcon.iconset

# Create the app bundle and DMG
jpackage \
  --name $APP_NAME \
  --app-version $APP_VERSION \
  --input target \
  --main-jar $(basename $MAIN_JAR) \
  --main-class heartsync.HeartSync \
  --type dmg \
  --icon AppIcon.icns \
  --mac-package-name $APP_NAME \
  --vendor "HeartSync Team" \
  --copyright "Copyright © 2024 HeartSync Team" \
  --description "HeartSync Dating Application" \
  --mac-package-identifier "com.heartsync.app"

# Clean up temporary files
rm -rf AppIcon.iconset AppIcon.icns 