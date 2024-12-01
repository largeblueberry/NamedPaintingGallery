pluginManagement {
    repositories {
        google() // Google Maven 저장소
        mavenCentral() // Maven Central 저장소
        gradlePluginPortal() // Gradle Plugin Portal
    }
    plugins {
        id("com.google.firebase.crashlytics") version "2.9.9" // Crashlytics 플러그인 버전 명시
        id("com.google.gms.google-services") version "4.4.0"

    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NamedPaintingGallery"
include(":app")