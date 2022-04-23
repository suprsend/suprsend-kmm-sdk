buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.google.com")
    }
    dependencies {
        classpath(Deps.JetBrains.Kotlin.gradlePlugin)
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Deps.JetBrains.Kotlin.VERSION}")
        classpath(Deps.Squareup.SQLDelight.gradlePlugin)
        classpath ("com.google.gms:google-services:4.3.10")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven(url ="https://jitpack.io")
        maven(url = "https://maven.google.com")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}