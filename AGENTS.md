# AGENTS.md — The Needed Utility

Project-level agent instructions. Overrides the workspace `AGENTS.md` for
work inside this folder.

## Stack (do not assume otherwise)

- **Kotlin + Jetpack Compose**, Material 3 with Material You dynamic colors
- **AGP 9 with built-in Kotlin** — the `org.jetbrains.kotlin.android` plugin
  must NOT be applied. Only `com.android.application` and
  `org.jetbrains.kotlin.plugin.compose` are applied. Kotlin version is the one
  bundled with AGP; the Compose Compiler plugin version must stay aligned
  with it.
- Navigation Compose, DataStore (Preferences). No network — the app is
  offline-first by design.
- minSdk 29, compileSdk/targetSdk 37. Java 17 bytecode.

## Layout

```
app/src/main/java/com/github/mkikets99/neededutility/
├── MainActivity.kt              # entry point (edge-to-edge, hosts Compose)
├── NeededUtilityApp.kt          # NavHost + theme wiring + Routes
├── data/                        # SettingsRepository, ToolPreferences (DataStore)
├── tools/
│   ├── ToolRegistry.kt          # ALL tools + groups are registered here
│   ├── converter/               # UnitConverter.kt (logic) + screen
│   ├── calculator/              # CalculatorEngine.kt, CalculatorHistory.kt + screen
│   ├── percentage/ bases/ roman/ color/ speed/ fractions/ geometry/ prime/ words/
│   ├── tip/ discount/ loan/ unitprice/ fuel/ savings/ electricity/ trip/ fuellog/ salary/
│   ├── bmi/ age/ bmr/
│   ├── notes/ text/ gpa/
│   ├── stopwatch/ multitimer/ worldclock/ datediff/ countdown/ sunrise/
│   ├── torch/ compass/ level/
│   ├── qr/                      # scanner (+ history), generator (text/WiFi/vCard), builders
│   ├── randomizer/ metronome/ morse/ teams/
└── ui/
    ├── common/CommonComponents.kt  # shared ResultRow / ResultRowText
    ├── common/Alerts.kt         # vibrateDevice + showCompletionNotification
    ├── home/HomeScreen.kt       # grouped, extensible tool grid
    ├── settings/SettingsScreen.kt
    └── theme/Theme.kt           # M3 + dynamic color
```

## Build & verify

```bash
./gradlew build          # full gate: compile + unit tests + lint
./gradlew assembleDebug  # quick compile check
./gradlew testDebugUnitTest
```

A task is done only when `./gradlew build` passes. Deploy to a **physical
device only** (`./gradlew installDebug`) — never use emulators. If no device
is connected, build-only is the accepted fallback.

## Conventions

- **Pure logic goes in plain Kotlin classes** (e.g. `CalculatorEngine`,
  `UnitConverter`) with unit tests in `app/src/test/`. Screens stay thin.
- **Use `ui/common/CommonComponents.kt`** (`ResultRow`, `ResultRowText`) for
  label/value result rows instead of private copies.
- **Adding a tool** = 3 steps: create `tools/<name>/` with logic + screen,
  add an entry to `ToolRegistry`, add a route in `NeededUtilityApp.kt`.
  The home screen picks it up automatically (grouped by `ToolGroup`).
- Match the existing style: Material 3 components, `@OptIn(ExperimentalMaterial3Api::class)`
  only where required (TopAppBar), English strings inline (single-language app).
- Keep dependencies minimal; ask the user before adding any new library.
- Update `STATUS.md` and `status.json` after meaningful changes.
- Plan new work in `STAGE.md` (staged / deferred / candidates). When an item
  ships, move it out of STAGE.md into STATUS.md.

## Gotchas

- `local.properties` is machine-local (git-ignored) and holds `sdk.dir`.
- **Release signing**: `keystore/release.keystore` + `keystore.properties`
  (git-ignored). The signing password is in `keystore.properties` on this
  machine — keep it safe. Builds fall back to debug signing without it.
- Torch tool requires a real camera flash; it gracefully reports when absent.
- Timer-like tools must use `ui/common/Alerts.kt` (`vibrateDevice`,
  `showCompletionNotification`) for completion feedback.
- Lint is non-fatal (`abortOnError = false`); check
  `app/build/reports/lint-results-debug.html` before shipping.
- **ZXing API notes** (learned the hard way): result parsing is
  `com.journeyapps.barcodescanner.ScanContract` / `ScanOptions`
  (`ActivityResultContract<ScanOptions, ScanIntentResult>`); the
  `com.google.zxing.integration.android.IntentIntegrator` class is deprecated.
- **Clipboard**: use `LocalClipboard.current.setClipEntry(ClipEntry(ClipData...))`;
  `LocalClipboardManager` is deprecated.

## Approved dependencies (ask user before adding more)

- androidx (core-ktx, activity-compose, lifecycle-*, compose BOM, material3,
  material-icons-extended, navigation-compose, datastore-preferences)
- `com.journeyapps:zxing-android-embedded` 4.3.0 + `com.google.zxing:core` 3.5.4
  (QR scanning/generation — user-approved)
- junit 4 (tests)
- **Pre-approved, not yet added** (user-requested 2026-11-10, see STAGE.md
  monetization track): Google Mobile Ads SDK (`play-services-ads`) for S-12
  and Play Billing (`billing-client`) for S-13. Use test ad units in debug.
