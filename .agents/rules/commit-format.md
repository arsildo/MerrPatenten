---
trigger: model_decision
description: Enforce Conventional Commits in terse caveman-commit format for all git commit messages
---

# Commit Message Format

Every commit message (human or AI) must follow Conventional Commits in terse `caveman-commit` style: intent and why over what, no fluff.

## Subject Line
- Format: `<type>(<scope>): <imperative summary>` (`<scope>` optional)
- Types: `feat`, `fix`, `refactor`, `perf`, `docs`, `test`, `chore`, `build`, `ci`, `style`, `revert`
- Imperative mood: `add`, `fix`, `remove` — not `added`, `adds`, `adding`
- Target ≤50 chars, hard cap 72 chars, no trailing period

## Body (only when needed)
- Skip body when subject is self-explanatory
- Always include body for: breaking changes (`!` + `BREAKING CHANGE:` footer), security fixes, data migrations, reverts, or non-obvious *why*
- Wrap lines at 72 chars; use `-` for bullets
- Reference issues at end: `Closes #42`, `Refs #17`

## Forbidden
- Filler: "This commit does X", "I", "we", "now", "currently"
- Emoji or AI-attribution banners (unless a trailer is explicitly required)
- Restating filenames when `<scope>` already names them
