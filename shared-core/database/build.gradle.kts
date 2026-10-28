@file:OptIn(
    org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl::class,
)

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.ksp)
    alias(libs.plugins.koinCompiler)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.core.database"
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
            moduleName = "Database"
            rootPackage = "com.arsildo.merrpatenten.shared.core.database"
            xcodeIntegration()
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedCore.model)
            implementation(projects.sharedCore.designSystem)

            api(libs.room.runtime)
            api(libs.sqlite.bundled)
            implementation(libs.kotlinx.coroutinesCore)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
