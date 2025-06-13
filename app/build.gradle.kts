plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services) // Apply the Google Services plugin here

}

android {
    namespace = "com.example.fot_news"
    compileSdk = 35 // Your compile SDK version

    defaultConfig {
        applicationId = "com.example.fot_news"
        minSdk = 24 // Your minimum SDK version
        targetSdk = 35 // Your target SDK version
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

    compileOptions {
        // Recommended Java version for compileSdk 34+ and modern AGP
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx) // Core KTX provides useful extensions

    // Firebase Platform (BOM) - Manages all Firebase library versions
    implementation(platform(libs.firebase.bom)) // Firebase BOM for managing dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.0.0")) // Firebase BOM to manage versions

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Firebase Realtime Database (if needed)
    implementation("com.google.firebase:firebase-database")

    // Firebase Analytics (Optional)
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Specific Libraries (versions managed by the BOM)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx) // Include if you're using ViewModel & LiveData

    // Glide for Image Loading
    implementation("com.github.bumptech.glide:glide:4.13.0") // Latest version of Glide for image loading
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0") // For Glide's annotation processing

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
