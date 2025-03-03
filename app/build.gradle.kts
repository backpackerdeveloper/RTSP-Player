plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.backpacker.rstpstream"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.backpacker.rstpstream"
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
    buildFeatures{
        viewBinding = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.media3.exoplayer.rtsp)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("androidx.media3:media3-exoplayer:1.5.1")
    implementation ("androidx.media3:media3-ui:1.5.1")
    implementation ("com.google.android.exoplayer:exoplayer-smoothstreaming:2.19.1")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7") // Ensure ViewModelScope is available
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
}