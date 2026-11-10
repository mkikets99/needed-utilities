# STATUS — The Needed Utility

_Last updated: 2026-11-10_

## Goal

An offline-first multi-tool toolbox for Android: "everything that can be
fitted inside", with a home screen designed to host more and more tools over
time, organized in groups.

## Current state

v5 complete and building. **38 tools** on the extensible grouped home screen:

- **Calculations:** Unit Converter, Calculator (+ history), Percentage,
  Base Converter, Roman Numerals, Color Converter, Speed · Distance · Time,
  Fractions, Geometry, Prime Checker
- **Money:** Tip & Split (+ rounding), Discount, Loan (EMI), Unit Price,
  Fuel Calculator, Savings & Interest, Electricity Cost, Trip & Expenses
- **Health:** BMI Calculator, Age Calculator, Daily Calories (BMR)
- **Productivity:** Notes (search + tags), Text Toolkit, GPA Calculator
- **Time:** Stopwatch & Timer (presets, vibration + notification), World
  Clock, Date Diff, Countdown, Sunrise & Sunset
- **Device:** Flashlight, QR Scanner (+ searchable history), QR Generator
  (+ WiFi QR mode), Compass, Bubble Level
- **Fun & Random:** Randomizer (dice d4–d20, coin, numbers, passwords),
  Metronome, Morse Code, Team Splitter

App shell: Material 3 + Material You, dark mode + theme settings,
Navigation Compose, DataStore light persistence. Package
`com.github.mkikets99.neededutility`, minSdk 29 / targetSdk 37.

## Build status

**Passing.** `./gradlew build` succeeds with zero compiler warnings.
Verified 2026-11-10 (JDK 27, Gradle 9.8.2, AGP 9.5.1, SDK platform 37).

Unit tests: **139** across 27 test classes — all green. The suite covers all
pure logic including the sunrise equation (equinox/polar/latitude cases).

## Refactoring

`ui/common/CommonComponents.kt` hosts shared `ResultRow` / `ResultRowText`;
new tool screens must use these (see AGENTS.md).

## Known issues / TODO

- Lint is non-fatal; hardcoded UI strings produce warnings (accepted).
- Countdown has no notifications yet (timer has them).
- Metronome timing is practice-grade; sensor tools lack calibration UI.
- No instrumented/UI tests; release build is unsigned.

## Next steps

1. **Password manager** (deferred by user decision).
2. Countdown notifications; note search by tag chips.
3. Candidates: cooking presets, BMI history, fuel log, tip history,
   unit-converter favorites, QR history export.
4. Release signing config + first signed build.
5. More shared extraction (input fields, top bars).
