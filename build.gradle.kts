plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    dependencies {
        // Android Gradle Plugin
        classpath("com.android.tools.build:gradle:8.1.1")
        // Google Services Plugin
        classpath("com.google.gms:google-services:4.3.15")
    }
}