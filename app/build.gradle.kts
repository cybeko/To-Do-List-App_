plugins {
    alias(libs.plugins.android.application)
    kotlin("android") version "2.1.20"
    kotlin("kapt") version "2.1.20"
}

android {
    namespace = "com.example.proj_final"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.proj_final"
        minSdk = 21
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"  // !!!
    }
    buildFeatures {
        viewBinding = true // !!!
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.karumi:dexter:6.2.3")
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.v172)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    val roomVersion = "2.5.2"

    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    val lifecycleVersion = "2.6.2"

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

}