# STAGE.md — Planned Items

_Roadmap of everything planned, deferred, or merely imagined for
The Needed Utility. Last updated: 2026-11-10 (after v6)._

**How this file works:**

| Status | Meaning |
|---|---|
| 🟡 **Staged** | Approved and waiting to be built (next session picks these) |
| 🔵 **Deferred** | Blocked on a user decision or external dependency |
| ⚪ **Candidate** | Idea only — promote to Staged when confirmed |
| ✅ **Done** | Shipped — recorded in `STATUS.md`, not tracked here |

Convention: when work starts, mark the item **in progress**; when it ships,
move it to `STATUS.md` and remove it from this file (keep this file a
forward-looking plan only).

---

## 🟡 Staged — next up

| ID | Item | Description | Notes |
|---|---|---|---|
| S-01 | Shared input components | Extract `NumberField` / `ToolTopBar` into `ui/common` like `ResultRow` | Pure refactor; pattern has repeated 20+ times |
| S-02 | Countdown exact alarms | Use `SCHEDULE_EXACT_ALARM` where permitted, keep inexact fallback | Android 12+ permission nuance |
| S-03 | Metronome upgrades | Tap tempo + finer timing (frame-clock based instead of `delay`) | Practice-grade → better |
| S-04 | Sensor calibration UI | Compass offset calibration + level "zero" button | Device tools polish |
| S-05 | Converter favorites | Pin favorite unit pairs to the top of Unit Converter | Uses existing DataStore |
| S-06 | Export / import app data | One JSON backup of all DataStore stores (notes, trips, fuel log…) | Pairs with S-07 |
| S-07 | CSV export | Export fuel log & trip expenses as CSV via share sheet | Depends on S-06 shape |
| S-08 | Fuel log: vehicles | Multiple named vehicles, each with its own fill-up log | Extends `FuelLogRepository` |
| S-09 | BMI history | Track weight/BMI over time (small DataStore log + trend list) | Health group |
| S-10 | Recipe scaler | Scale ingredient quantities by servings | New tool, Cooking-ish group or Time |

## 🔵 Deferred — waiting on a decision

| ID | Item | Blocked on | Notes |
|---|---|---|---|
| D-01 | **Password manager** (vault) | User decision (deferred since v2) | Password *generator* already ships in Randomizer |
| D-02 | Currency conversions | Online rates → breaks "offline-first" vow | Need user decision: on-device rates file vs network |
| D-03 | Play Store release | Publishing intent | `bundleRelease` + signing are ready (v6); needs listing assets, screenshots, privacy policy |
| D-04 | Localization | Scope decision | UI strings are inline English today (single-language app by design) |

## ⚪ Candidates — unconfirmed ideas

| ID | Item | Description |
|---|---|---|
| C-01 | QR / vCard gallery | Save & reuse generated QR payloads |
| C-02 | Tip history | Remember past tip calculations |
| C-03 | Countdown widgets | Home-screen widget showing nearest event |
| C-04 | Note attachments | Keep notes text-only; images would need scoped storage work |
| C-05 | Pro-grade metronome | Accent beats, time signatures, subdivision |
| C-06 | Unit-aware arithmetic | "3 km + 500 m" style typed calculator |
| C-07 | Health: heart-rate via camera | PPG through camera flash — fun, noisy, needs calibration |
| C-08 | Screenshot-friendly tool cards | Share a tool result as an image card |
| C-09 | App shortcuts | Long-press launcher shortcuts for favorite tools |
| C-10 | Minification + R8 | Turn on `isMinifyEnabled` with tuned ProGuard rules before Play release |

## 🧱 Technical / infra (continuous)

| ID | Item | Notes |
|---|---|---|
| T-01 | Keep `AGENTS.md` conventions current | Update when architecture changes (e.g. S-01) |
| T-02 | Keep STATUS.md + status.json in sync with every milestone | Workspace policy |
| T-03 | Keep zero compiler warnings + all tests green as hard gate | `./gradlew build` |
| T-04 | Instrumented/UI tests (Compose testing) | Currently only JVM unit tests (159) |
| T-05 | CI build (GitHub Actions) | Runs `./gradlew build` on push; repo has no remote yet |

## Suggested build order

1. **S-01** (refactor, unlocks faster tool work) → **S-02 → S-03 → S-04**
   (tool polish)
2. **S-05 → S-06 → S-07 → S-08 → S-09 → S-10** (new features)
3. Then revisit **D-02 / D-03** once the user weighs in.

---

_Maintenance rule: every shipped item lands in `STATUS.md`; every new idea
lands here as a Candidate. Do not let this file become a graveyard — prune
ideas that are rejected._