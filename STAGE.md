# STAGE.md — Planned Items

_Roadmap of everything planned, deferred, or merely imagined for
The Needed Utility. Last updated: 2026-11-10 (post D/C review)._

**How this file works:**

| Status | Meaning |
|---|---|
| 🟡 **Staged** | Approved and waiting to be built (next session picks these) |
| 🔵 **Deferred** | Blocked on a user decision or external dependency |
| ⚪ **Candidate** | Idea only — promote to Staged when confirmed |
| ✅ **Done** | Shipped — recorded in `STATUS.md`, not tracked here |

Convention: when work starts, mark the item **in progress**; when it ships,
move it to `STATUS.md` and remove it from this file (keep this file a
forward-looking plan only).

---

## 🟡 Staged

### Core polish

| ID | Item | Description | Notes |
|---|---|---|---|
| S-02 | Countdown exact alarms | `SCHEDULE_EXACT_ALARM` where permitted, inexact fallback | Android 12+ nuance |
| S-03 | Metronome upgrades | Tap tempo + frame-clock timing | — |
| S-04 | Sensor calibration UI | Compass offset + level "zero" button | — |

### Tool features

| ID | Item | Description | Notes |
|---|---|---|---|
| S-05 | Converter favorites | Pin favorite unit pairs to top | Existing DataStore |
| S-06 | Export / import app data | One JSON backup of all stores | — |
| S-07 | CSV export | Fuel log & trips via share sheet | With S-06 |
| S-08 | Fuel log: vehicles | Multiple named vehicles | Extends repo |
| S-09 | BMI history | Weight/BMI over time + trend | Health group |
| S-10 | Recipe scaler | Scale ingredients by servings | — |
| S-14 | Currency converter | Online rates API, **cached 7 days, refresh on connectivity** | Decided 2026-11-10; adds INTERNET permission (rates only) |
| S-15 | Password manager | Encrypted local vault: master password + entries + copy | Decided 2026-11-10; needs crypto design (Android Keystore) |
| S-16 | Localization prep | Move all strings to `strings.xml`, ready for en, en-GB, uk, pl, de, fr, es… | Decided 2026-11-10; languages come later |

### Monetization (plumbing first — user decided 2026-11-10)

| ID | Item | Description | Notes |
|---|---|---|---|
| S-13 | One-time "Pro" paywall | Removes ads + lifts free-tier caps | Deps pre-approved: `billing-client`; **generous caps: 25 notes / 25 events / 100 entries** |

> Publish-to-Play is **not** required to build S-12/S-13 — use test ads and
> Play Billing test flow first (D-03 stays deferred until publishing intent).

## Monetization

- **AdMob integrated (v8)**: banner on home (collapses until loaded — no gray
  placeholder) + interstitial **every 5th tool switch** (any tool open counts,
  re-opening the same tool included — `AdFrequency` unit-tested). Keys live in
  `ad-mob.txt` → `BuildConfig` (user's real IDs since 2026-11-10). Ads are
  best-effort: offline or failed loads never affect tools.
- **Pro (one-time purchase, S-13 — next)**: removes all ads + lifts caps.
  No subscriptions. One shared `isPro` gate; generous caps (25 notes / 25
  events / 100 entries).

## 🔵 Deferred

| ID | Item | Blocked on | Notes |
|---|---|---|---|
| D-03 | Play Store release | Publishing intent | Needed only for real ads/IAP revenue. `bundleRelease` + signing ready (v6) |
| — | Heart-rate / sound-meter privacy docs | If C-07/C-22 ship | Mic/camera use needs privacy policy text for Play |

## ⚪ Candidates (confirmed backlog — promote opportunistically)

All reviewed 2026-11-10 and **approved as candidates**:

| ID | Item | Description |
|---|---|---|
| C-01 | QR / vCard gallery | Save & reuse generated QR payloads |
| C-02 | Tip history | Remember past tip calculations |
| C-03 | Countdown widget | Home-screen widget with nearest event |
| C-04 | Note attachments | Images in notes (scoped storage work) |
| C-05 | Pro metronome | Accents, time signatures, subdivisions |
| C-06 | Unit-aware math | Typed calculator: "3 km + 500 m" |
| C-07 | Heart-rate camera | PPG via camera flash (noisy, fun) |
| C-08 | Result cards | Share tool result as image card |
| C-09 | App shortcuts | Long-press launcher shortcuts for tools |
| C-10 | R8 minification | Minify + tuned ProGuard before Play release |
| C-11 | Pomodoro timer | Focus sessions with breaks + alerts |
| C-12 | Sound meter | Mic-based rough decibel meter |
| C-13 | Drawing pad | Finger drawing, save as image |
| C-14 | Decision wheel | Animated spin wheel to pick options |
| C-15 | Barcode generator | EAN/UPC via existing ZXing |
| C-16 | Tally counter | Simple clicker / tally marks |
| C-17 | Battery & device info | Charge cycles, health, runtime stats |
| C-18 | Screen ruler | Measure small objects against the display |
| C-19 | Typing speed test | WPM + accuracy challenge |
| C-20 | Flashlight SOS / morse blink | Torch blinking patterns |
| C-21 | Habit tracker | Daily check-off streaks |
| C-22 | White noise generator | Ambient sounds for focus/sleep |

## 🧱 Technical / infra (continuous)

| ID | Item | Notes |
|---|---|---|
| T-01 | Keep `AGENTS.md` conventions current | Update when architecture changes |
| T-02 | Keep STATUS.md + status.json in sync with every milestone | Workspace policy |
| T-03 | Keep zero compiler warnings + all tests green as hard gate | `./gradlew build` |
| T-04 | Instrumented/UI tests (Compose testing) | Currently only JVM unit tests |
| T-05 | CI build (GitHub Actions) | No remote yet |
| T-06 | Privacy policy draft | Required before Play release (ads, IAP, mic/camera tools) |

## Suggested build order

1. ~~S-01 → S-11~~ ✅ done (v7); ~~S-12~~ ✅ done (v8)
2. **S-02 → S-03 → S-04** (tool polish) and **S-16** (localization prep)
3. **S-05 → S-06 → S-07 → S-08 → S-09 → S-10 → S-14 → S-15**
4. **S-13** (Pro paywall — now gates the shipped ads)
5. Candidates promoted from the backlog as capacity allows; revisit **D-03**
   when ready to publish.

---

_Maintenance rule: every shipped item lands in `STATUS.md`; every new idea
lands here as a Candidate. Do not let this file become a graveyard — prune
ideas that are rejected._