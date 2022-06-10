buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.17.1")
        //ASM8 error when up to date
        classpath("com.google.firebase:perf-plugin:1.4.1")
        //Firebase init random crash at newer
        classpath("com.google.gms:google-services:4.3.4")
    }
}

plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlinVersion apply false
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}