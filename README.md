# The Needed Utility

Offline-first multi-tool toolbox for Android. One app that aims to eventually
hold **everything that can be fitted inside** — a growing set of small, focused
utilities on one extensible home screen.

## v1 tools

| Tool | Group | Description |
|---|---|---|
| Unit Converter | Calculations | Length, mass, temperature, volume |
| Calculator | Calculations | Everyday arithmetic (immediate execution) |
| Tip & Split | Money | Tip percentage and bill splitting |
| Stopwatch & Timer | Time | Count-up with laps + countdown timer |
| Flashlight | Device | Camera torch toggle |

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
