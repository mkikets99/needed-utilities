# STATUS — The Needed Utility

_Last updated: 2026-11-09_

## Goal

An offline-first multi-tool toolbox for Android: "everything that can be
fitted inside", with a home screen designed to host more and more tools over
time, organized in groups.

## Current state

v2 complete and building. Ten tools on the extensible grouped home screen:

- **Calculations:** Unit Converter (persists units), Calculator
- **Money:** Tip & Split (persists tip % and split), Discount
- **Productivity:** Notes (DataStore + JSON, add/edit/delete)
- **Time:** Stopwatch (laps) & Timer (countdown with vibration + notification)
- **Device:** Flashlight, QR Scanner (ZXing), QR Generator (ZXing)
- **Fun & Random:** Randomizer (dice, coin, random number, password generator
  with copy-to-clipboard)

App shell: Material 3 + Material You dynamic colors, dark mode + theme
settings (persisted), Navigation Compose, DataStore light persistence.
Package `com.github.mkikets99.neededutility`, minSdk 29 / targetSdk 37.

Deployed and smoke-tested on a physical device (Samsung Galaxy S24 Ultra,
Android 17) — home screen, navigation, and torch verified live.

## Build status

**Passing.** `./gradlew build` succeeds (debug + release APKs, unit tests,
lint) with zero compiler warnings. Verified 2026-11-09 on Windows with JDK 27,
Gradle 9.8.2, AGP 9.5.1 (built-in Kotlin), Android SDK platform 37.

Unit tests: 15 (CalculatorEngineTest, UnitConverterTest) — all green.

## Known issues / TODO

- Lint is non-fatal; hardcoded UI strings produce warnings (single-language
  app — accepted for v1).
- Notes are stored as one JSON blob — fine for small lists, revisit if notes
  grow large (Room would be the upgrade path).
- Calculator has no history; expression display is current-value only.
- No instrumented/UI tests.
- Release build is unsigned (no release signing config yet).

## Next steps

1. **Password manager** (deferred by user decision) — vault-style tool;
   the password *generator* already ships inside Randomizer.
2. Calculator history; converter favorites.
3. More tools: unit-aware conversions (currency needs rates → online mode?),
   QR history, note search/tags.
4. Release signing config + first signed build.
5. Optional: extract repeated result-row / dropdown patterns into shared
   composables as the tool count grows.
