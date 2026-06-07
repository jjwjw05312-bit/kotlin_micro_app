#!/bin/bash

echo "🚀 Building the app..."
./gradlew assembleDebug

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi

echo "🧹 Purani app ko poori tarah delete (uninstall) kar rahe hain..."
adb uninstall com.example.microapp > /dev/null 2>&1

echo "📲 Nayi fresh app ko emulator mein install kar rahe hain..."
# -r means replace existing application
adb install -r app/build/outputs/apk/debug/app-debug.apk

if [ $? -ne 0 ]; then
    echo "❌ Installation failed. Make sure your emulator is running."
    exit 1
fi

echo "✨ Launching the app..."
adb shell monkey -p com.example.microapp -c android.intent.category.LAUNCHER 1 > /dev/null 2>&1

echo "✅ All done! Aapki app emulator mein khul gayi hai."
