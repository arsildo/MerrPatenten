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
        compileSdk = 37
        minSdk = 31
        androidResources.enable = true
    }

    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.material3)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.annotation)
            api(libs.kotlinx.datetime)
        }
    }
}
