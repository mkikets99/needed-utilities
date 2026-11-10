# ProGuard / R8 rules for The Needed Utility (C-10, prepared 2026-11-10)
#
# Stack: Jetpack Compose, DataStore, Navigation, Google Mobile Ads,
# Play Billing, ZXing (journeyapps). Most AndroidX/Compose libraries ship
# consumer rules in their AARs; the rules below are the deliberate,
# belt-and-suspenders keep for the third-party pieces.

# --- Crash readability -------------------------------------------------
# Keep file names / line numbers in stack traces (no class names kept).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- Google Mobile Ads (S-12) ------------------------------------------
# AdMob loads ad plumbing through its SDK; keep it all. The AAR also ships
# consumer rules, this is defense in depth.
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# --- Google Play Billing (S-13) ----------------------------------------
# Purchase/acknowledge callbacks and product details flow through the SDK.
-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**

# --- ZXing (QR scanning/generation) ------------------------------------
# CaptureActivity is manifest-referenced (kept automatically); the scanner
# internals and core codec are kept to be safe.
-keep class com.journeyapps.barcodescanner.** { *; }
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

# --- Kotlin / coroutines -----------------------------------------------
# (kotlinx-coroutines ships consumer rules; silence optional warnings)
-dontwarn kotlinx.coroutines.**

# --- General notes -----------------------------------------------------
# * App code (com.github.mkikets99.neededutility.**) needs NO keep rules:
#   it uses no reflection; names/structure are obfuscated on purpose.
# * If a future tool uses reflection/serialization, add targeted keeps here
#   and test the RELEASE build on a physical device before shipping.
