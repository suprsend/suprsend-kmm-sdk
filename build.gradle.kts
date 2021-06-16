buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.10")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath(Deps.Squareup.SQLDelight.gradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}