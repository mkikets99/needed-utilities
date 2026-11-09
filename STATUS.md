# STATUS — The Needed Utility

_Last updated: 2026-11-10_

## Goal

An offline-first multi-tool toolbox for Android: "everything that can be
fitted inside", with a home screen designed to host more and more tools over
time, organized in groups.

## Current state

v3 complete and building. **21 tools** on the extensible grouped home screen:

- **Calculations:** Unit Converter, Calculator (now with history),
  Percentage, Base Converter
- **Money:** Tip & Split, Discount, Loan (EMI), Unit Price
- **Health:** BMI Calculator
- **Productivity:** Notes (DataStore + JSON), Text Toolkit
- **Time:** Stopwatch & Timer (vibration + notification), World Clock
- **Device:** Flashlight, QR Scanner (with scan history), QR Generator,
  Compass, Bubble Level
- **Fun & Random:** Randomizer (dice, coin, numbers, password generator with
  strength meter), Metronome, Morse Code

App shell: Material 3 + Material You dynamic colors, dark mode + theme
settings (persisted), Navigation Compose, DataStore light persistence.
Package `com.github.mkikets99.neededutility`, minSdk 29 / targetSdk 37.

Previously smoke-tested on a physical device (Samsung Galaxy S24 Ultra,
Android 17) at v1; v3 is build-verified (device was disconnected).

## Build status

**Passing.** `./gradlew build` succeeds (debug + release APKs, unit tests,
lint) with zero compiler warnings. Verified 2026-11-10 on Windows with JDK 27,
Gradle 9.8.2, AGP 9.5.1 (built-in Kotlin), Android SDK platform 37.

Unit tests: **60** across 9 test classes (engine, converter, percentage,
bases, loan, unit price, BMI, text, morse, password strength) — all green.
The tests caught and fixed a real `titleCase` whitespace bug during development.

## Known issues / TODO

- Lint is non-fatal; hardcoded UI strings produce warnings (single-language
  app — accepted).
- Notes are stored as one JSON blob — fine for small lists (Room is the
  upgrade path if needed).
- Metronome timing uses `delay()` — accurate enough for practice, not for
  professional musicians.
- Compass/level accuracy depends on device sensors; no calibration UI yet.
- No instrumented/UI tests.
- Release build is unsigned (no release signing config yet).

## Next steps

1. **Password manager** (deferred by user decision) — vault-style tool;
   the password *generator* already ships inside Randomizer.
2. Converter favorites; calculator history export/copy.
3. More candidates: date diff / countdown-to-date, cooking timer presets,
   fuel efficiency, tip history, note search/tags.
4. Release signing config + first signed build.
5. Shared composables extraction (result rows, input fields) — the pattern
   has now repeated 10+ times.
