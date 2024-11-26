plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services") // Firebase plugin
    kotlin("kapt")
}

android {
    namespace = "com.largeblueberry.namedpaintinggallery"
    compileSdk = 34 // 최신 API Level로 업데이트

    defaultConfig {
        applicationId = "com.largeblueberry.namedpaintinggallery"
        minSdk = 26 // 최소 SDK 그대로 유지
        targetSdk = 34 // Google Play 요구사항 충족
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
    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase Libraries (using BOM)
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // ViewPager2 and RecyclerView
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.recyclerview)

    // Glide for image loading
    implementation(libs.glide)
    kapt(libs.glide)

    // Palette for extracting image colors
    implementation(libs.androidx.palette)

    // Test Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
