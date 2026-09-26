@file:OptIn(
    org.jetbrains.kotlin.gradle.export.ExperimentalExportDsl::class,
    org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl::class,
)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.core.model"
        compileSdk = 37
        minSdk = 31
    }

    iosArm64()
    iosSimulatorArm64()
    export {
        swift {
            moduleName = "Model"
            rootPackage = "com.arsildo.merrpatenten.shared.core.model"
            xcodeIntegration()
        }
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.androidx.annotation)
        }
    }
}
