// Top-level build file. Plugin versions are declared in gradle/libs.versions.toml.
// Kotlin compilation comes from AGP 9's built-in Kotlin (kotlin-android is NOT applied).
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
