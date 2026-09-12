plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.koinCompiler)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared.core.data"
        compileSdk = 37
        minSdk = 31
    }

    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedCore.model)
            implementation(projects.sharedCore.database)
            implementation(projects.sharedCore.datastore)

            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.kotlinx.datetime)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutinesTest)
        }
    }
}
