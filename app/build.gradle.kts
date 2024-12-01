plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services") // Firebase plugin
    id("com.google.firebase.crashlytics") // Firebase Crashlytics plugin
    kotlin("kapt") // Kotlin annotation processing
}

android {
    namespace = "com.largeblueberry.namedpaintinggallery"
    compileSdk = 34 // 최신 API Level

    defaultConfig {
        applicationId = "com.largeblueberry.namedpaintinggallery"
        minSdk = 26 // 최소 SDK 설정
        targetSdk = 34 // Google Play 요구사항 충족
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
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
    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase Libraries (using BOM for versioning)
    implementation(platform("com.google.firebase:firebase-bom:33.0.0")) // 최신 BOM 버전
    implementation("com.google.firebase:firebase-auth") // Firebase Authentication
    implementation("com.google.firebase:firebase-firestore-ktx") // Firestore
    implementation("com.google.firebase:firebase-storage-ktx") // Firebase Storage
    implementation("com.google.firebase:firebase-analytics-ktx") // Analytics
    implementation("com.google.firebase:firebase-crashlytics-ktx") // Crashlytics

    // Google Sign-In Library
    implementation("com.google.android.gms:play-services-auth:20.7.0") // Google 로그인

    // ViewPager2 and RecyclerView
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.recyclerview)

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")

    // Palette for extracting image colors
    implementation("androidx.palette:palette:1.0.0")

    // Lifecycle components
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Preferences
    implementation(libs.androidx.preference)

    // Annotations
    implementation(libs.androidx.annotation)

    // Test Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.android.material:material:1.9.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}

apply(plugin = "com.google.gms.google-services") // Firebase 기본 플러그인 적용
apply(plugin = "com.google.firebase.crashlytics") // Firebase Crashlytics 플러그인 적용