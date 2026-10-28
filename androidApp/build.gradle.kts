import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koinCompiler)
}

android {
    namespace = "com.arsildo.merrpatenten"

    compileSdk = 37
    compileSdkMinor = 1
    defaultConfig {
        applicationId = "com.arsildo.merrpatenten"
        minSdk = 31
        versionCode = 37
        versionName = "3.0.5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            val localProps = Properties().apply {
                val localPropsFile = rootProject.file("local.properties")
                if (localPropsFile.exists()) {
                    load(localPropsFile.inputStream())
                }
            }

            val keystorePath = System.getenv("KEYSTORE_FILE") ?: localProps.getProperty("KEYSTORE_FILE")
            val keystorePassword = System.getenv("KEYSTORE_PASSWORD") ?: localProps.getProperty("KEYSTORE_PASSWORD")
            val keyAlias = System.getenv("KEY_ALIAS") ?: localProps.getProperty("KEY_ALIAS")
            val keyPassword = System.getenv("KEY_PASSWORD") ?: localProps.getProperty("KEY_PASSWORD")

            if (!keystorePath.isNullOrEmpty() &&
                file(keystorePath).exists() &&
                !keystorePassword.isNullOrBlank() &&
                !keyAlias.isNullOrBlank() &&
                !keyPassword.isNullOrBlank()
            ) {
                storeFile = file(keystorePath)
                storePassword = keystorePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            val releaseSigning = signingConfigs.getByName("release")
            if (releaseSigning.storeFile != null) {
                signingConfig = releaseSigning
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

kotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.lifecycleRuntimeCompose)

    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.startup)
    implementation(projects.sharedCore.designSystem)
    implementation(projects.sharedUi)
}
