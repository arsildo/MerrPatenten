plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.feature.exam"
        compileSdk = 37
        minSdk = 31
        androidResources.enable = true
    }

    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedCore.model)
            implementation(projects.sharedCore.designSystem)
            implementation(projects.sharedCore.navigation)
            implementation(projects.sharedCore.data)
            implementation(projects.sharedCore.datastore)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.lifecycleRuntimeCompose)
            implementation(libs.androidx.lifecycleViewmodelNavigation3)
            implementation(libs.androidx.navigation3Ui)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.coroutinesCore)
        }
    }
}
