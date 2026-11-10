# STATUS — The Needed Utility

_Last updated: 2026-11-10_

## Goal

An offline-first multi-tool toolbox for Android: "everything that can be
fitted inside", with a home screen designed to host more and more tools over
time, organized in groups.

## Current state

v4 complete and building. **30 tools** on the extensible grouped home screen:

- **Calculations:** Unit Converter, Calculator (+ history, copy-all),
  Percentage, Base Converter, Roman Numerals, Color Converter
- **Money:** Tip & Split, Discount, Loan (EMI), Unit Price, Fuel Calculator,
  Savings & Interest, Electricity Cost
- **Health:** BMI Calculator, Age Calculator, Daily Calories (BMR)
- **Productivity:** Notes (search, DataStore + JSON), Text Toolkit
- **Time:** Stopwatch & Timer (vibration + notification), World Clock,
  Date Diff, Countdown (named events, live countdown)
- **Device:** Flashlight, QR Scanner (+ scan history), QR Generator,
  Compass, Bubble Level
- **Fun & Random:** Randomizer (dice d4–d20, coin, numbers, password
  generator + strength), Metronome, Morse Code

App shell: Material 3 + Material You, dark mode + theme settings,
Navigation Compose, DataStore light persistence. Package
`com.github.mkikets99.neededutility`, minSdk 29 / targetSdk 37.

## Build status

**Passing.** `./gradlew build` succeeds (debug + release APKs, unit tests,
lint) with zero compiler warnings. Verified 2026-11-10 on Windows with JDK 27,
Gradle 9.8.2, AGP 9.5.1 (built-in Kotlin), Android SDK platform 37.

Unit tests: **93** across 18 test classes — all green. Tests caught real bugs
(titleCase whitespace in v3; wrong age-test expectation in v4 before build).

## Refactoring

`ui/common/CommonComponents.kt` now hosts the shared `ResultRow` /
`ResultRowText` components; 9 calculator-style screens were deduplicated.
**New tool screens must use these instead of private copies.**

## Known issues / TODO

- Lint is non-fatal; hardcoded UI strings produce warnings (accepted).
- Notes stored as one JSON blob (Room is the upgrade path if lists grow).
- Metronome timing uses `delay()` — practice-grade, not pro-grade.
- Sensor tools (compass/level) have no calibration UI yet.
- No instrumented/UI tests; release build is unsigned.

## Next steps

1. **Password manager** (deferred by user decision).
2. Date picker UX polish; countdown notifications (like the timer).
3. More candidates: cooking timer presets, trip expense tracker, note tags,
   QR history search, tip rounding.
4. Release signing config + first signed build.
5. Further shared extraction (input fields, top bars) as tools keep growing.
