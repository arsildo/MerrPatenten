---
trigger: model_decision
description: Enforce power-of-2 dp values (2, 4, 8, 16, 32, 64) for all UI paddings and ContentPadding
---

# Power-of-2 Padding Scale

All UI padding values must use powers of 2 (`0.dp`, `2.dp`, `4.dp`, `8.dp`, `16.dp`, `32.dp`, `64.dp`, etc.) for visual consistency across screens and components.

## Rules
- Use power-of-2 `dp` values (`2.dp`, `4.dp`, `8.dp`, `16.dp`, `32.dp`, `64.dp`) in `Modifier.padding(...)` and `PaddingValues(...)`.
- Never use arbitrary or off-scale padding values such as `3.dp`, `6.dp`, `10.dp`, `12.dp`, `14.dp`, `15.dp`, `20.dp`, or `24.dp`.
- Applies strictly to padding (`Modifier.padding`, `PaddingValues`). Component and icon dimensions (`Modifier.size(24.dp)`, `Modifier.height(...)`, `Modifier.width(...)`) are exempt.
- Disallowed example: `contentPadding = PaddingValues(14.dp)`
- Allowed example: `contentPadding = PaddingValues(16.dp)` or `contentPadding = PaddingValues(8.dp)`
