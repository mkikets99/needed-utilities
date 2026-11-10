# The Needed Utility

Offline-first multi-tool toolbox for Android. One app that aims to eventually
hold **everything that can be fitted inside** — a growing set of small, focused
utilities on one extensible home screen.

## Tools (v1–v5)

| Tool | Group | Description |
|---|---|---|
| Unit Converter | Calculations | Length, mass, temperature, volume |
| Calculator | Calculations | Arithmetic with persistent history |
| Percentage | Calculations | P% of X, what %, % change |
| Base Converter | Calculations | Binary, octal, decimal, hex |
| Roman Numerals | Calculations | Number ↔ roman |
| Color Converter | Calculations | Hex ↔ RGB with live swatch |
| Speed · Distance · Time | Calculations | Any two → third |
| Fractions | Calculations | Fraction ↔ decimal |
| Geometry | Calculations | Areas & perimeters (circle, rect, triangle) |
| Prime Checker | Calculations | Primes & factorization |
| Tip & Split | Money | Tips, splitting, total rounding |
| Discount | Money | Sale price and savings |
| Loan Calculator | Money | EMI, interest, total cost |
| Unit Price | Money | Compare pack value |
| Fuel Calculator | Money | Trip cost & consumption |
| Savings & Interest | Money | Compound growth with deposits |
| Electricity Cost | Money | Appliance running cost |
| Trip & Expenses | Money | Track & split shared costs |
| BMI Calculator | Health | Body mass index & healthy range |
| Age Calculator | Health | Age, days lived, next birthday |
| Daily Calories | Health | BMR & maintenance calories |
| Notes | Productivity | Local notes with search & tags |
| Text Toolkit | Productivity | Counts, case transforms, reverse |
| GPA Calculator | Productivity | US 4.0 weighted GPA |
| Stopwatch & Timer | Time | Laps, presets, vibration & notification |
| World Clock | Time | Times around the world |
| Date Diff | Time | Days between dates |
| Countdown | Time | Named events with live countdown |
| Sunrise & Sunset | Time | Solar times anywhere (NOAA) |
| Flashlight | Device | Camera torch toggle |
| QR Scanner | Device | Scan QR codes, searchable history |
| QR Generator | Device | Text/URL and WiFi QR codes |
| Compass | Device | Magnetic heading dial |
| Bubble Level | Device | Surface level with live bubble |
| Randomizer | Fun & Random | Dice (d4–d20), coin, numbers, passwords |
| Metronome | Fun & Random | Beat at any tempo (30–240 BPM) |
| Morse Code | Fun & Random | Encode & decode morse |
| Team Splitter | Fun & Random | Random balanced teams |

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
