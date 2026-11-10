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

- `ui/common/` — shared components **new screens must use**:
  `ResultRow`/`ResultRowText`, `ToolTopBar` (title + back + actions),
  `NumberField`, `Alerts.kt` (`vibrateDevice`, `showCompletionNotification`).
- Countdown notifications: AlarmManager + `CountdownReceiver`, with
  `CountdownBootReceiver` re-scheduling after reboot (09:00 local on event date).
- Settings: theme mode, dynamic color, **home layout (Cards / List)** — all
  persisted via DataStore.

## Monetization

- **AdMob live (v8)**: home-screen banner (collapses until an ad loads — no
  gray placeholder) + interstitial **every 5th tool switch**, where any tool
  open counts and re-opening the same tool included (`AdFrequency`, unit
  tested). Keys in `ad-mob.txt` → `BuildConfig`; ads are best-effort and
  never affect tools offline.
- **Pro paywall live (v9)**: one-time purchase (`billing-client`, product id
  `pro`) — removes all ads (banner + interstitials gated on `ProState.isPro`)
  and lifts caps (25 notes / 25 events / 100 fuel / 100 trip entries,
  `Limits` unit-tested). Purchase state cached in DataStore (offline-proof),
  re-synced from Play on startup; paywall dialog from Settings and whenever
  a cap is hit (Buy + Restore buttons; shows live price when available).

## Known issues / TODO

- **Play Console setup pending** (D-03): billing requires a managed product
  with id `pro`; until defined, purchases no-op gracefully. Real ad units
  are already in `ad-mob.txt`. Privacy policy drafted
  (`PRIVACY_POLICY.md`) — host it at a public URL before submission.
- Countdown alarms are inexact; a permission prompt for exact alarms could
  be added later (Android 12+ SCHEDULE_EXACT_ALARM).
- Lint is non-fatal; hardcoded UI strings produce warnings (accepted).
- Metronome timing is practice-grade; sensor tools lack calibration UI.
- No instrumented/UI tests.

## Next steps

See **`STAGE.md`** for the full roadmap (staged / deferred / candidates).
Highlights staged next: shared input components, countdown exact alarms,
metronome tap tempo, sensor calibration, converter favorites, data export,
fuel-log vehicles, BMI history, recipe scaler.
