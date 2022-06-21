buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlytics}")
        //ASM8 error when up to date
        classpath("com.google.firebase:perf-plugin:${Versions.firebasePerf}")
        //Firebase init random crash at newer
        classpath("com.google.gms:google-services:${Versions.googleServices}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detect}")
    }
}

plugins {
    id("com.android.application") version Versions.android apply false
    id("com.android.library") version Versions.android apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false
    id("com.github.ben-manes.versions") version Versions.gradleVersions
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}