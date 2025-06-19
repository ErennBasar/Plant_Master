// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("androidx.navigation.safeargs") version "2.6.0" apply false
}
buildscript {
    dependencies {

        classpath("com.android.tools.build:gradle:7.4.2")
    }
}
