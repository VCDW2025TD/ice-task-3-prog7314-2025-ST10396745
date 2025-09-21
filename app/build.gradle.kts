plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.memestreamproto"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.memestreamproto"
        minSdk = 24
        targetSdk = 35
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

    // enable desugaring so java.time works on API 24+
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.activity)
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Moshi & Retrofit
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:gifdecoder:4.16.0")
    // kapt("com.github.bumptech.glide:compiler:4.16.0") // optional

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // Firebase services
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Biometrics
    implementation("androidx.biometric:biometric:1.1.0")

    // Optional extras
    implementation("com.google.android.libraries.places:places:2.5.0")
    implementation("com.burhanrashid52:photoeditor:3.0.2")

    // Room (offline cache) — bumped to 2.7.0
    implementation("androidx.room:room-runtime:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")
    kapt("androidx.room:room-compiler:2.7.0")

    // WorkManager (background sync)
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // Desugar JDK libs → enables java.time on API 24+
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")
}
