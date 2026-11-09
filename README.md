# The Needed Utility

Offline-first multi-tool toolbox for Android. One app that aims to eventually
hold **everything that can be fitted inside** — a growing set of small, focused
utilities on one extensible home screen.

## Tools (v1–v3)

| Tool | Group | Description |
|---|---|---|
| Unit Converter | Calculations | Length, mass, temperature, volume |
| Calculator | Calculations | Arithmetic with persistent history |
| Percentage | Calculations | P% of X, what %, % change |
| Base Converter | Calculations | Binary, octal, decimal, hex |
| Tip & Split | Money | Tip percentage and bill splitting |
| Discount | Money | Sale price and savings |
| Loan Calculator | Money | EMI, interest, total cost |
| Unit Price | Money | Compare pack value |
| BMI Calculator | Health | Body mass index & healthy range |
| Notes | Productivity | Local notes (DataStore + JSON) |
| Text Toolkit | Productivity | Counts, case transforms, reverse |
| Stopwatch & Timer | Time | Laps + countdown with vibration & notification |
| World Clock | Time | Times around the world |
| Flashlight | Device | Camera torch toggle |
| QR Scanner | Device | Scan QR codes (ZXing), with scan history |
| QR Generator | Device | Turn text/URLs into QR codes |
| Compass | Device | Magnetic heading dial |
| Bubble Level | Device | Surface level with live bubble |
| Randomizer | Fun & Random | Dice, coin, numbers, password generator |
| Metronome | Fun & Random | Beat at any tempo (30–240 BPM) |
| Morse Code | Fun & Random | Encode & decode morse |

The home screen renders tools **grouped by category** from a central registry
(`tools/ToolRegistry.kt`), so new tools can be added over time — including new
groups — without redesigning the UI.

## Tech stack

- Kotlin + Jetpack Compose (Material 3, Material You dynamic colors, dark mode)
- AGP 9 with [built-in Kotlin](https://developer.android.com/build/migrate-to-built-in-kotlin)
- Navigation Compose, DataStore (Preferences) for light persistence
- minSdk 29 (Android 10), compileSdk/targetSdk 37

## Build & run

```bash
./gradlew build          # compile + unit tests + lint (CI gate)
./gradlew assembleDebug  # debug APK
./gradlew installDebug   # install on a connected physical device
```

The app is fully offline: no network permission, no remote services.

## Documentation

- `AGENTS.md` — agent instructions for working in this project
- `STATUS.md` / `status.json` — current project status (human / machine readable)
