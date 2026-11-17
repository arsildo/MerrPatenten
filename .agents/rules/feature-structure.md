---
trigger: model_decision
description: Standard internal folder and package layout for modules under shared-feature/
---

# Feature Module Structure

Every module under `shared-feature/<feature-name>/` must follow this internal layout and package structure.

## Directory & Package Layout
Base package: `com.arsildo.merrpatenten.shared.feature.<feature>` (omit hyphens in package name, e.g. `image-details` -> `imagedetails`).

```text
shared-feature/<feature-name>/
├── build.gradle.kts
└── src/commonMain/kotlin/com/arsildo/merrpatenten/shared/feature/<feature>/
    ├── <Feature>Screen.kt
    ├── <Feature>ViewModel.kt
    ├── di/
    │   └── <Feature>Module.kt
    ├── navigation/
    │   └── <Feature>Entry.kt
    ├── ui/                  # UI components/helpers shared within the feature
    │   └── <SharedUi>.kt
    └── <subfeature>/        # sensible domain packages (e.g. legend/, map/)
        └── <SubFeature>.kt
```

## Rules
- Put the main `<Feature>Screen.kt` and `<Feature>ViewModel.kt` directly in the feature root package (`...feature.<feature>`).
- Put Koin module definitions in `di/<Feature>Module.kt`.
- Put Navigation 3 route/entry registration in `navigation/<Feature>Entry.kt`.
- Organize sub-features into sensible domain-named subpackages beside the main screen (e.g. `exam/` -> `legend/`, `map/`).
- Put UI elements shared across screens/sub-features within the feature inside `ui/`.
- Configure `build.gradle.kts` as a KMP module (`android`, `iosArm64`, `iosSimulatorArm64` with `export { swift { ... } }`, `jvm`) using `projects.sharedCore.*`, Compose Multiplatform, Navigation 3, and Koin (`koinCompiler`).
