@file:OptIn(
    org.jetbrains.kotlin.gradle.export.ExperimentalExportDsl::class,
    org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl::class,
)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.feature.imagedetails"
        compileSdk = 37
        minSdk = 31
        androidResources.enable = true
    }

    iosArm64()
    iosSimulatorArm64()
    export {
        swift {
            moduleName = "ImageDetails"
            rootPackage = "com.arsildo.merrpatenten.shared.feature.imagedetails"
            xcodeIntegration()
        }
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedCore.designSystem)
            implementation(projects.sharedCore.navigation)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.navigation3Ui)
            implementation(libs.androidx.lifecycleRuntimeCompose)
        }
    }
}
