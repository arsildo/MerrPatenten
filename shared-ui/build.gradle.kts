plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.ui"
        compileSdk = 37
        minSdk = 31
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SharedUi"
            isStatic = true
        }
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared"))

            implementation(project(":shared-core:model"))
            implementation(project(":shared-core:design-system"))
            implementation(project(":shared-core:navigation"))
            implementation(project(":shared-core:datastore"))
            implementation(project(":shared-core:database"))
            implementation(project(":shared-core:data"))

            implementation(project(":shared-feature:dashboard"))
            implementation(project(":shared-feature:exam"))
            implementation(project(":shared-feature:image-details"))
            implementation(project(":shared-feature:statistics"))
            implementation(project(":shared-feature:preferences"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)

            implementation(libs.androidx.navigation3Ui)
            implementation(libs.androidx.lifecycleViewmodelNavigation3)
            implementation(libs.androidx.lifecycleRuntimeCompose)
            implementation(libs.androidx.savedstate.compose)

            implementation(libs.androidx.datastorePreferencesCore)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.coroutinesCore)

            implementation(project.dependencies.platform(libs.coil.bom))
            implementation(libs.coil.compose)

            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs.compose)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}
