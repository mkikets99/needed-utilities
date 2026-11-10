# STATUS — The Needed Utility

_Last updated: 2026-11-10_

## Goal

An offline-first multi-tool toolbox for Android: "everything that can be
fitted inside", with a home screen designed to host more and more tools over
time, organized in groups.

## Current state

v6 complete and building. **42 tools** on the extensible grouped home screen:

- **Calculations (11):** Unit Converter, Calculator (+ history), Percentage,
  Base Converter, Roman Numerals, Color Converter, Speed · Distance · Time,
  Fractions, Geometry, Prime Checker, Number to Words
- **Money (10):** Tip & Split (+ rounding), Discount, Loan (EMI), Unit Price,
  Fuel Calculator, Savings & Interest, Electricity Cost, Trip & Expenses,
  Fuel Log (fill-ups & consumption), Salary Converter
- **Health (3):** BMI, Age, Daily Calories (BMR)
- **Productivity (3):** Notes (search + tags + tag chips), Text Toolkit, GPA
- **Time (6):** Stopwatch & Timer (presets, alerts), Multi Timer (concurrent
  named timers + kitchen presets), World Clock, Date Diff, Countdown
  (notifications!), Sunrise & Sunset
- **Device (5):** Flashlight, QR Scanner (searchable history + copy all),
  QR Generator (text, WiFi and contact/vCard modes), Compass, Bubble Level
- **Fun & Random (4):** Randomizer, Metronome, Morse Code, Team Splitter

## Build status

**Passing** with zero compiler warnings (2026-11-10).
Unit tests: **159** across 31 test classes — all green.

**Release signing is configured.** `app-release.apk` is signed with a local
keystore (`keystore/release.keystore` + `keystore.properties`, both
git-ignored; password lives in keystore.properties). Verified with apksigner.
Builds fall back to debug signing when no keystore is present.

## Architecture notes

- `ui/common/` — shared `ResultRow`/`ResultRowText` and `Alerts.kt`
  (`vibrateDevice`, `showCompletionNotification`) used by timer tools.
- Countdown notifications: AlarmManager + `CountdownReceiver`, with
  `CountdownBootReceiver` re-scheduling after reboot (09:00 local on event date).

## Known issues / TODO

- Countdown alarms are inexact; a permission prompt for exact alarms could
  be added later (Android 12+ SCHEDULE_EXACT_ALARM).
- Lint is non-fatal; hardcoded UI strings produce warnings (accepted).
- Metronome timing is practice-grade; sensor tools lack calibration UI.
- No instrumented/UI tests.

## Next steps

1. **Password manager** (deferred by user decision).
2. Fuel log: per-vehicle logs; CSV export.
3. Candidates: cooking recipe scaler, BMI history, converter favorites,
   QR/vCard gallery, unit-aware currency (needs rates → online mode decision).
4. Play Store prep (listing assets, AAB build — `bundleRelease` ready).
5. More shared extraction (input fields, top bars).
