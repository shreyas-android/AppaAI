// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.0")
    }
}

plugins {
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}
true // Needed to make the Suppress annotation work for the plugins block
