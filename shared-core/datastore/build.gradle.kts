@file:OptIn(
    org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl::class,
)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.koinCompiler)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.core.datastore"
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
            moduleName = "Datastore"
            rootPackage = "com.arsildo.merrpatenten.shared.core.datastore"
            xcodeIntegration()
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedCore.designSystem)

            api(libs.androidx.datastorePreferencesCore)
            api(libs.androidx.datastoreCore)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
