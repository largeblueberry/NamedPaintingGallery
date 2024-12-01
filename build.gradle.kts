plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Android Gradle Plugin (이미 plugins 블록에서 선언했으므로 필요하지 않을 경우 제거 가능)
        classpath("com.android.tools.build:gradle:8.1.1")
        // Google Services Plugin (이미 plugins 블록에서 선언했으므로 필요하지 않을 경우 제거 가능)
        classpath("com.google.gms:google-services:4.3.15")
    }
}
