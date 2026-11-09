# STATUS — The Needed Utility

_Last updated: 2026-11-09_

## Goal

An offline-first multi-tool toolbox for Android: "everything that can be
fitted inside", with a home screen designed to host more and more tools over
time, organized in groups.

## Current state

v1 complete and building. Five tools ship on an extensible grouped home screen:

- Unit Converter (length / mass / temperature / volume) — remembers last-used units
- Calculator (immediate execution, ±, %)
- Tip & Split (tip presets + slider, bill split) — remembers tip % and split count
- Stopwatch & Timer (laps, countdown)
- Flashlight (torch toggle, permission-aware, graceful no-flash fallback)

App shell: Material 3 + Material You dynamic colors, dark mode + theme
settings (persisted), Navigation Compose, DataStore light persistence.
Package `com.github.mkikets99.neededutility`, minSdk 29 / targetSdk 37.

## Build status

**Passing.** `./gradlew build` succeeds (debug + release APKs, unit tests,
lint). Verified 2026-11-09 on Windows with JDK 27, Gradle 9.8.2,
AGP 9.5.1 (built-in Kotlin), Android SDK platform 37.

Unit tests: 15 (CalculatorEngineTest, UnitConverterTest) — all green.
Not yet deployed to a device (no physical device connected at time of work;
build-only fallback per workspace policy).

## Known issues / TODO

- Lint is non-fatal; hardcoded UI strings produce warnings (single-language
  app — accepted for v1).
- Timer end is silent (no alarm/vibration yet).
- Calculator has no history; expression display is current-value only.
- No instrumented/UI tests.
- Release build is unsigned (no release signing config yet).

## Next steps

1. Deploy to a physical device and smoke-test torch + navigation.
2. Timer completion feedback (vibration / notification).
3. More tools behind the same registry (QR scanner, notes, randomizer,
   discount calculator, ...).
4. Optional: calculator history, converter favorites.
5. Release signing config + first signed build.
