// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias (libs.plugins.android.application) apply false
    alias (libs.plugins.kotlin.android) apply false
    alias (libs.plugins.ksp) apply false
    alias (libs.plugins.detekt)
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }
}