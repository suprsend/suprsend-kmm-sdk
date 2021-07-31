buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath(Deps.JetBrains.Kotlin.gradlePlugin)
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Deps.JetBrains.Kotlin.VERSION}")
        classpath(Deps.Squareup.SQLDelight.gradlePlugin)
        classpath ("com.google.gms:google-services:4.3.8")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven(url ="https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}