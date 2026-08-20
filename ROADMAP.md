# 🗺️ MerrPatenten — Product Roadmap & Feature Plan

This document outlines the planned feature additions, technical considerations, and implementation milestones for **MerrPatenten**.

---

## 📌 Status Overview

- [ ] **Phase 1: Study & Training Modes**
- [x] **Phase 2: Road Sign & Intersection Encyclopedia** *(Core Catalog & Navigation implemented)*
- [ ] **Phase 3: Advanced Analytics & Exam Readiness**
- [ ] **Phase 4: Examination & UX Enhancements**
- [ ] **Phase 5: Export & Driving School Tools**

---

## 🚀 Detailed Feature Specifications

### 1. 📚 Study & Training Modes (Beyond Simulation)

- [ ] **Mistake Notebook ("Gabimet e Mia" / Weak Spot Practice)**
  - Automatically persist incorrectly answered questions in the local Room database.
  - Dedicated screen/quiz mode that loads *only* past mistakes.
  - Automatically remove questions from the mistake bank once answered correctly 2 or 3 consecutive times.
- [ ] **Topic-Based Practice (Praktikë sipas Kapitujve)**
  - Group question bank into official Albanian DPSHTRR curriculum modules:
    - *Sinjalistika Rrugore* (Road Signs)
    - *Përparësia në Kryqëzime* (Right of Way & Intersections)
    - *Shpejtësia dhe Distanca e Sigurisë* (Speed & Safety Distance)
    - *Rregullat e Qarkullimit & Manovrat* (Traffic Rules & Maneuvers)
    - *Ndihma e Parë & Rreziqet* (First Aid & Road Safety)
    - *Sinjalet e Policit & Dritat* (Traffic Police & Lights)
  - Allow users to practice individual chapters before attempting full 40-question simulations.
- [ ] **Quick Blitz Mode (10 Pyetje të Shpejta)**
  - Rapid, bite-sized 10-question practice mode designed for quick daily commutes or warm-ups.
- [ ] **Bookmarked Questions (Pyetje të Ruajtura / Favorite)**
  - Star / save tricky or ambiguous questions during an exam or review for quick revision.

---

### 2. 📖 Road Sign & Intersection Encyclopedia

- [x] **Interactive Sign Dictionary (Katalogu i Sinjaleve)**
  - Browsable and searchable visual dictionary of official Albanian road signs.
  - Categorized into:
    - Sinjale Rreziku (Warning Signs)
    - Sinjale Urdhëruese / Ndaluese (Prohibitory & Mandatory Signs)
    - Sinjale Treguese / Udhëzuese (Information & Direction Signs)
    - Sinjalistika Horizontale (Road Markings)
    - Sinjalet e Policit të Trafikut (Traffic Police Hand Signals)
- [x] **Intersection Priority Encyclopedia ("Kush kalon i pari?")**
  - Diagram scenarios explaining vehicle right-of-way sequences with official exam question correlation.
- [ ] **Intersection Priority Simulator (Interactive Tap Ordering)**
  - Interactive diagram scenarios where users tap vehicles in the correct order of right-of-way.

---

### 3. 📊 Advanced Analytics & Exam Readiness

- [ ] **DPSHTRR Readiness Score (% Gatishmëria për Provim)**
  - Smart readiness metric calculated from:
    - Pass rate over the last 10–20 exams
    - Topic coverage percentage
    - Mistake resolution rate
  - Visual gauge on the dashboard: *"Gatishmëria: 85% — Gati për provim zyrtar!"*
- [ ] **Topic Accuracy Breakdown (Analizë sipas Temave)**
  - Visual breakdown (progress bars / heatmaps) showing strengths and weaknesses per category (e.g., *95% Sinjale, 60% Kryqëzime*).

---

### 4. ⌨️ Examination & UX Enhancements

- [ ] **Desktop Keyboard Shortcuts (Për JVM Desktop Target)**
  - Fast keyboard shortcuts for desktop study:
    - `S` / `1` → **Saktë** (True)
    - `G` / `2` → **Gabim** (False)
    - `←` / `→` → Previous / Next Question
    - `M` / `Space` → Open Question Map
    - `Z` → Zoom Sign Image
    - `Enter` → Confirm / Finish Exam
- [ ] **Answer Explanations (Pse është e saktë / e gabuar?)**
  - Short legal/Highway Code (*Kodi Rrugor*) explanation accompanying each question during the review phase.
- [ ] **Audio Text-to-Speech (Leximi me Zë i Pyetjes)**
  - Accessibility audio playback option to read questions and answers aloud in Albanian.

---

### 5. 📄 Export & Driving School Integration

- [ ] **PDF Scorecard Export & Share**
  - Generate a downloadable/shareable PDF summary of completed tests and mistake reviews.
- [ ] **Instructor Report (Për Autoshkollat)**
  - Export test history and readiness score to share with driving school instructors.

---

## 🏗️ Architecture & Database Impact

### Database Extensions (`shared-core:database`)
* `mistake_records` table: `(question_id, error_count, consecutive_correct, last_tested_at)`
* `bookmarked_questions` table: `(question_id, bookmarked_at, note)`
* `topics` table / metadata: `(topic_id, name_sq, name_en, category)`

### Modularization Plan (`shared-feature`)
* `shared-feature:mistakes` — Mistake notebook & targeted quiz runner
* `shared-feature:catalog` — [COMPLETED] Road signs & rules encyclopedia
* `shared-feature:topics` — Topic-based practice mode
