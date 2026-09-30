# 🧭 Navigation 3 & Adaptive Architecture Roadmap

Technical modernization plan for Navigation 3 and Adaptive layouts across all platforms (Android, iOS, Desktop).

---

## 📌 Milestones Overview

- [x] **Milestone 1: Root Navigation & Adaptive Shell** [COMPLETED]
  - Replace flat root backstack with multi-stack `NavigationState` and `Navigator` controller.
  - Independent backstacks per top-level tab (single-top ordering, exit-through-home, tab reselection events).
  - Integrate adaptive navigation container (adaptive `NavigationBar` & `NavigationRail`).
  - Remove redundant floating toolbar from `DashboardScreen`.
  - Manage navigation area visibility during full-screen flows (`Exam`, `ZoomableImage`).
- [x] **Milestone 2: App Bar Collapsing & Scroll Behaviors** [COMPLETED]
  - Implement `TopAppBarDefaults.enterAlwaysScrollBehavior()` across all feature screens.
  - Connect nested scroll connections to `Scaffold` modifiers.
- [ ] **Milestone 3: Adaptive Grids & Multi-Pane Polish**
  - Migrate `StatisticsScreen` dual-pane history list from `LazyColumn` to adaptive `LazyVerticalGrid`.
  - Migrate `ExamResultBottomSheet` grid from `GridCells.Fixed(5)` to `GridCells.Adaptive(minSize = 56.dp)`.
  - Evaluate supporting pane for `Mistakes` on tablet/desktop.
  - Add `@FormFactorPreviews` for multi-window, tablet, and foldable validation.
- [ ] **Milestone 4: Anti-Patterns & Codebase Hygiene**
  - Fix deprecated `rememberTransformableState` in `ZoomableExamImage`.
  - Delete dead `Disclaimer : NavKey` in `DashboardEntry.kt`.
  - Remove or wire unused `onImageDetailsClick` in `CatalogRoute`.
  - Clean up hardcoded insets zeroing across `Scaffold` instances.

---

## 🛠️ Milestone 1 Details: Root Navigation & Adaptive Shell

### Problems
1. Single flat `rememberNavBackStack` creates an unbounded linear history when switching top-level sections (`Dashboard`, `Catalog`, `Mistakes`, `Statistics`, `Preferences`).
2. `DashboardScreen` relies on an ad-hoc bottom `HorizontalFloatingToolbar`. Once navigated away, top-level navigation disappears completely.
3. Tablets and desktop form factors lack a persistent side `NavigationRail`.

### Architecture Target
1. **Top-Level Navigation**:
   - Top-level destinations: `Dashboard`, `Catalog`, `Mistakes`, `Statistics`, `Preferences`.
   - On compact screens (phones): Bottom navigation bar.
   - On medium / expanded screens (tablets, foldables unfolded, desktop): Side navigation rail.
2. **Dynamic Visibility**:
   - Navigation rail / bar automatically hidden when in active exam simulation (`Exam`) or full-screen image inspection (`ZoomableImage`).
3. **Cleaned Dashboard**:
   - Remove floating action bar from `DashboardScreen`.
