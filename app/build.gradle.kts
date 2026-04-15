plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") // Ya tomará la 2.0.0 definida arriba
    id("com.google.gms.google-services")
}

android {
    namespace = "com.android.system.service.assistance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.system.service.assistance"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Firebase - Mantengo tu BoM 33.1.2
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
    // AÑADIMOS STORAGE para poder subir las fotos y videos
    implementation("com.google.firebase:firebase-storage")

    // GPS y LOCALIZACIÓN (Google Play Services)
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // CameraX - El motor para la Cámara y Video
    val camerax_version = "1.3.4"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    // UI y Componentes Base
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}