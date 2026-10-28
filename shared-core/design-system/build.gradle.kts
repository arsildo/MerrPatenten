@file:OptIn(
    org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl::class,
)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

compose.resources {
    publicResClass = true
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.core.designsystem"
        compileSdk {
            version = release(37) {
                minorApiLevel = 1
            }
        }
        minSdk = 31
        androidResources.enable = true
    }

    iosArm64()
    iosSimulatorArm64()
    export {
        swift {
            moduleName = "DesignSystem"
            rootPackage = "com.arsildo.merrpatenten.shared.core.designsystem"
            xcodeIntegration()
        }
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.material3)
            api(libs.compose.material3.adaptive)
            api(libs.compose.material3.adaptive.layout)
            implementation(libs.compose.materialIconsExtended)

            implementation(libs.androidx.annotation)
            api(libs.kotlinx.datetime)
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }
    }
}
