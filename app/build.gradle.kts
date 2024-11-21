plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services") // Firebase plugin
    kotlin("kapt")
}

android {
    namespace = "com.largeblueberry.namedpaintinggallery"
    compileSdk = 28 // API Level 28

    defaultConfig {
        applicationId = "com.largeblueberry.namedpaintinggallery"
        minSdk = 21 // 최소 SDK 버전 설정
        targetSdk = 28 // API Level 28
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true // ViewBinding 활성화
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // AndroidX Core (API 28 호환)
    implementation("androidx.core:core:1.10.1")
    implementation("androidx.core:core-ktx:1.10.1")

    // AndroidX 라이브러리
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase Libraries (using BOM)
    implementation(platform("com.google.firebase:firebase-bom:31.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Glide 이미지 로더
    implementation("com.github.bumptech.glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    // Palette (이미지 색상 추출)
    implementation("androidx.palette:palette:1.0.0")

    // Test Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}