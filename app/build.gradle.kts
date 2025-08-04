plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services") // Add this line for Firebase
}

android {
    namespace = "com.somed.apacheivscore"
    compileSdk = 33
    
    defaultConfig {
        applicationId = "com.somed.apacheivscore"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Existing dependencies
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    
    // Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")
    
    // Google Sign In SDK
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    
    // Required for viewModels()
    implementation("androidx.activity:activity-ktx:1.7.2")
    
    // Optional: Firebase UI for easier auth implementation
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    
    // Optional: For better coroutine support with Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    
    
    implementation("com.github.bumptech.glide:glide:4.15.1")
    // If you use Kotlin:
    //kapt("com.github.bumptech.glide:compiler:4.15.1")

}