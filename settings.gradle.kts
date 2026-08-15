enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://redirector.kotlinlang.org/maven/dev")
        maven("https://packages.jetbrains.team/maven/p/cmp/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        maven("https://redirector.kotlinlang.org/maven/dev")
        maven("https://packages.jetbrains.team/maven/p/cmp/dev")
        mavenCentral()
    }
}

include(":androidApp")
include(":desktopApp")

include(":shared")
include(":shared-ui")

include(":shared-core:model")
include(":shared-core:navigation")
include(":shared-core:datastore")
include(":shared-core:database")
include(":shared-core:data")
include(":shared-core:design-system")

include(":shared-feature:dashboard")
include(":shared-feature:exam")
include(":shared-feature:image-details")
include(":shared-feature:statistics")
include(":shared-feature:preferences")
