import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("kotlin-parcelize")
}

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Serialization
                implementation(Deps.JetBrains.Kotlin.serialization)
                implementation(Deps.JetBrains.Kotlin.datetime)
                // Ktor
                implementation(Deps.JetBrains.Ktor.clientCommon)
                // Ktor Features
                implementation(Deps.JetBrains.Ktor.serialization)
                implementation(Deps.JetBrains.Ktor.commonLogging)

                implementation(Deps.JetBrains.Coroutines.common) {
                    version {
                        strictly(Deps.JetBrains.Coroutines.VERSION)
                    }
                }
                implementation(Deps.Squareup.SQLDelight.coroutinesExtension)
                implementation(Deps.AndroidX.ANNOTATION)
                //Todo version constants
                implementation("com.google.code.gson:gson:2.8.6")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                api(Deps.JetBrains.Ktor.clientMock)
                api(Deps.JetBrains.Coroutines.common)
            }
        }
        val androidMain by getting {
            dependencies {

                implementation(Deps.JetBrains.Kotlin.testJunit)

                implementation(Deps.Squareup.SQLDelight.sqliteDriver)
                implementation(Deps.Squareup.SQLDelight.androidDriver)

                implementation(Deps.JetBrains.Ktor.clientAndroid)
                implementation(Deps.JetBrains.Coroutines.android)

                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
                implementation("androidx.core:core-ktx:1.5.0")

                implementation("com.google.firebase:firebase-analytics:19.0.0")
                implementation("com.google.firebase:firebase-messaging:22.0.0")


            }
        }
        val androidTest by getting {
            dependencies {


                implementation(Deps.JetBrains.Kotlin.testKotlin)
                /**/
                implementation(Deps.JetBrains.Kotlin.serialization)
                implementation(Deps.JetBrains.Coroutines.test)
                /**/
                implementation(Deps.AndroidX.Test.core)
                implementation(Deps.AndroidX.Test.junit)
                implementation(Deps.AndroidX.Test.runner)
                implementation(Deps.AndroidX.Test.rules)
//                implementation(kotlin("test-junit"))
//                implementation("junit:junit:4.13.2")

                implementation(Deps.Squareup.mockServer)
                implementation(Deps.Squareup.SQLDelight.sqliteDriver)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.nativeDriver)
                implementation(Deps.JetBrains.Ktor.clientIos)
                implementation(Deps.JetBrains.Coroutines.common) {
                    version {
                        strictly(Deps.JetBrains.Coroutines.VERSION)
                    }
                }
            }
        }
        val iosTest by getting
    }
}

sqldelight {
    database("SuprSendDatabase") { // This will be the name of the generated database class.
        packageName = "app.suprsend.android"
        schemaOutputDirectory = file("src/commonMain/sqldelight/databases")
        verifyMigrations = true
    }
}

android {
    compileSdkVersion(Deps.Android.compileSdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Deps.Android.minSdk)
        targetSdkVersion(Deps.Android.targetSdk)
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    val targetDir = File(buildDir, "xcode-frameworks")

    group = "build"
    dependsOn(framework.linkTask)
    inputs.property("mode", mode)

    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)