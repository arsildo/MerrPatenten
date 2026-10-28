# Project Environment

Inspected 2026-10-28 by `argent_environment_inspector`; project files confirm KMP targets (the generic workspace snapshot missed the app module naming).

```json
{
  "project_type": "kotlin_multiplatform_compose",
  "is_react_native": false,
  "is_native_android": true,
  "is_native_ios": true,
  "targets": [
    "android",
    "iosArm64",
    "iosSimulatorArm64",
    "jvm_desktop"
  ],
  "build_debug": "./gradlew :androidApp:assembleDebug",
  "build_bundle": "./gradlew :androidApp:bundleRelease",
  "desktop_run": "./gradlew :desktopApp:run",
  "lint": "./gradlew ktlintCheck",
  "tests": "./gradlew jvmTest",
  "android_min_sdk": 31,
  "compose_version": "1.13.10-alpha01+dev5041",
  "ios_integration": "Kotlin Swift export / Xcode integration; no CocoaPods",
  "shared_images_owner": "shared-core:design-system",
  "shared_images_loader": "org.jetbrains.compose.resources.painterResource",
  "qa": "GitHub Actions; no saved Argent flows found",
  "metro_port": null
}
```
