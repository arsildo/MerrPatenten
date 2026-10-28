@file:OptIn(
    org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl::class,
)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koinCompiler)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.ui"
        compileSdk {
            version = release(37) {
                minorApiLevel = 1
            }
        }
        minSdk = 31
    }
    iosArm64()
    iosSimulatorArm64()
    export {
        swift {
            moduleName = "SharedUi"
            rootPackage = "com.arsildo.merrpatenten.shared.ui"
            xcodeIntegration()
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)

            implementation(projects.sharedCore.model)
            implementation(projects.sharedCore.designSystem)
            implementation(projects.sharedCore.navigation)
            implementation(projects.sharedCore.datastore)
            implementation(projects.sharedCore.database)
            implementation(projects.sharedCore.data)

            implementation(projects.sharedFeature.dashboard)
            implementation(projects.sharedFeature.exam)
            implementation(projects.sharedFeature.imageDetails)
            implementation(projects.sharedFeature.statistics)
            implementation(projects.sharedFeature.preferences)
            implementation(projects.sharedFeature.catalog)
            implementation(projects.sharedFeature.mistakes)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
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
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}
