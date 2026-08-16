plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.core.navigation"
        compileSdk = 37
        minSdk = 31
    }

    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.material3)

            implementation(libs.androidx.navigation3Ui)
            api(libs.androidx.navigationCommon)
            implementation(libs.androidx.lifecycleRuntimeCompose)
            implementation(libs.androidx.lifecycleViewmodelCompose)
            implementation(libs.androidx.savedstate.compose)
            implementation(libs.kotlinx.serializationJson)
        }
    }
}
