---
trigger: model_decision
description: Enforce modular feature architecture where every new feature lives in its own Gradle module
---

# Modular Feature Architecture

Every new feature must live in its own Gradle module under `:shared-feature:<feature-name>` instead of adding feature code into existing feature modules, `:shared`, or app targets.

## Rules
- Create new features at `shared-feature/<feature-name>/` and register `include(":shared-feature:<feature-name>")` in `settings.gradle.kts`.
- Use lowercase kebab-case for module directory and Gradle path (e.g., `:shared-feature:image-details`).
- Keep feature UI, state, ViewModels, and feature-specific logic isolated inside its own `:shared-feature:<feature-name>` module.
- Depend on `:shared-core:*` modules (`model`, `navigation`, `datastore`, `database`, `data`, `design-system`) for shared contracts; avoid cross-feature module dependencies.
- Wire the new feature module into `:shared-ui` / `:shared` navigation and DI rather than placing feature implementation there.
