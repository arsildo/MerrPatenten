plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    android {
        namespace = "com.arsildo.merrpatenten.shared"
        compileSdk {
            version = release(37) {
                minorApiLevel = 1
            }
        }
        minSdk = 31
    }

    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
        }
    }
}
