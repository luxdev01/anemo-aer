/*
 * Copyright (c) 2021 2bllw8
 * SPDX-License-Identifier: GPL-3.0-only
 */
plugins {
    id("com.android.application")
    id("com.diffplug.spotless") version "6.5.1"
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = rootProject.extra["targetSdkVersion"] as Int
    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = 1723569000
        versionName = "2024.08.13"
        applicationId = "alt.nainapps.aer"
    }

    buildFeatures {
        buildConfig = false
    }

    compileOptions {
        sourceCompatibility = rootProject.extra["sourceCompatibilityVersion"] as JavaVersion
        targetCompatibility = rootProject.extra["targetCompatibilityVersion"] as JavaVersion
    }

    dependenciesInfo {
        includeInApk = false
    }

    signingConfigs {
        if (rootProject.ext.get("keyStoreFile") != null && (rootProject.ext.get("keyStoreFile") as File).exists()) {
            create("anemo") {
                storeFile = file(rootProject.ext.get("keyStoreFile") as String)
                storePassword = rootProject.ext.get("keyStorePassword") as String
                keyAlias = rootProject.ext.get("keyAlias") as String
                keyPassword = rootProject.ext.get("keyPassword") as String
            }
        }
    }

    buildTypes {
        val useAnemoConfig = rootProject.ext.get("keyStoreFile") != null && (rootProject.ext.get("keyStoreFile") as File).exists()

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")

            if (useAnemoConfig) {
                signingConfig = signingConfigs.getByName("anemo")
            }
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            if (useAnemoConfig) {
                signingConfig = signingConfigs.getByName("anemo")
            }
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "alt.nainapps.aer"
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    val androidxAnnotation = "androidx.annotation:annotation:1.8.0"
    val eitherLib = "io.github.2bllw8:either:3.4.0"

    implementation(androidxAnnotation)
    implementation(eitherLib)
}

afterEvaluate {
    val spotlessCheck = tasks.named("spotlessCheck")
    if (spotlessCheck.isPresent) {
        tasks.withType<JavaCompile>().configureEach {
            finalizedBy(spotlessCheck)
        }
    }
}

