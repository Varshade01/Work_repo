import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

val keystorePropsFile = rootProject.file("local.properties")
val keystoreProps = Properties().apply {
    if (keystorePropsFile.exists())
        load(FileInputStream(keystorePropsFile))
}

android {
    namespace = "com.maat.cha"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.maat.cha"
        minSdk = 28
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val path = keystoreProps.getProperty("MAATCHA_KEYSTORE")
                ?: throw GradleException("MAATCHA_KEYSTORE не вказаний в local.properties")
            storeFile = file(path)
            storePassword = keystoreProps.getProperty("MAATCHA_KEYSTORE_PASSWORD")
                ?: throw GradleException("MAATCHA_KEYSTORE_PASSWORD не вказаний")
            keyAlias = keystoreProps.getProperty("MAATCHA_KEY_ALIAS")
                ?: throw GradleException("MAATCHA_KEY_ALIAS не вказаний")
            keyPassword = keystoreProps.getProperty("MAATCHA_KEY_PASSWORD")
                ?: throw GradleException("MAATCHA_KEY_PASSWORD не вказаний")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    //splash
    implementation(libs.androidx.core.splashscreen)

    //
    implementation (libs.accompanist.systemuicontroller)


    //coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Network + JSON
    implementation(libs.retrofit)
    implementation(libs.moshi)
    implementation(libs.converter.moshi)
    implementation(libs.androidx.appcompat)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.logging.interceptor)

    //data store
    implementation(libs.androidx.datastore.preferences)

    //hilt-di
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

    //apps identifier
    implementation(libs.androidx.ads.identifier)

    //installreferrer
    implementation (libs.installreferrer)

    //apps flyer
    implementation(libs.af.android.sdk)

    //core-android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //compose-ui
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Coil for image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}