import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
//    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("kotlin-parcelize")
    id("maven-publish")
}
apply {
    from("$rootDir/ktlint.gradle")
}
kotlin {
    group = Deps.Publication.GROUP
    version = Deps.Publication.VERSION
    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }
//    cocoapods {
//        summary = "Core SuprSend Framework"
//        homepage = "https://github.com/suprsend/suprsend-kmm-sdk"
//        frameworkName = "SuprsendCore"
//
//        pod("Reachability")
//    }

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SuprsendCore"
            xcf.add(this)
        }
    }

    //macosX64
    //macosArm64
    //iosArm32 - iOS ARM 32
    //iosArm64 - iOS ARM 64
    //iosX64 - iOS Simulator (x86_64)
    //iosSimulatorArm64 - ARM64 simulator target


    sourceSets {

        val commonMain by getting {
            dependencies {
                // Serialization
                api(Deps.JetBrains.Kotlin.serialization)
                api(Deps.JetBrains.Kotlin.datetime)
                //implementation(Deps.JetBrains.Kotlin.datetime)
                // Ktor
                api(Deps.JetBrains.Ktor.clientCommon)
                // Ktor Features
                api(Deps.JetBrains.Ktor.serialization)
                api(Deps.JetBrains.Ktor.commonLogging)

                implementation(Deps.JetBrains.Coroutines.core) {
                    version {
                        strictly(Deps.JetBrains.Coroutines.VERSION)
                    }
                }
                implementation(Deps.Squareup.SQLDelight.coroutinesExtension)
                implementation(Deps.AndroidX.ANNOTATION)
                implementation("com.soywiz.korlibs.krypto:krypto:2.4.12")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                api(Deps.JetBrains.Ktor.clientMock)
                api(Deps.JetBrains.Coroutines.core)
            }
        }
        val androidMain by getting {
            dependencies {

                //implementation(Deps.JetBrains.Kotlin.testJunit)

                api(Deps.Squareup.SQLDelight.sqliteDriver)
                api(Deps.Squareup.SQLDelight.androidDriver)

                api(Deps.JetBrains.Ktor.clientAndroid)
                implementation(Deps.JetBrains.Coroutines.android)

                //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
                implementation(Deps.AndroidX.CORE_KTX)

                api("com.google.firebase:firebase-messaging:23.0.0")

                implementation("com.googlecode.libphonenumber:libphonenumber:8.12.38")
                implementation(files("libs/MiPush_SDK_Client_4_8_3.jar"))
            }
        }
        val androidTest by getting {
            dependencies {

                implementation(Deps.JetBrains.Kotlin.testKotlin)
                /**/
                implementation(Deps.JetBrains.Kotlin.serialization)
                implementation(Deps.JetBrains.Coroutines.test)
                /**/
                implementation(Deps.AndroidX.core)
                implementation(Deps.AndroidX.junit)
                implementation(Deps.AndroidX.runner)
                implementation(Deps.AndroidX.rules)
//                implementation(kotlin("test-junit"))
//                implementation("junit:junit:4.13.2")

                implementation(Deps.Squareup.mockServer)
                implementation("io.mockk:mockk:1.12.0")
                implementation(Deps.Squareup.SQLDelight.sqliteDriver)

            }
        }
        val iosX64Main by getting {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.nativeDriver)
                implementation(Deps.JetBrains.Ktor.clientIos)
                implementation(Deps.JetBrains.Coroutines.core) {
                    version {
                        strictly(Deps.JetBrains.Coroutines.VERSION)
                    }
                }
            }
        }
        val iosArm64Main by getting {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.nativeDriver)
                implementation(Deps.JetBrains.Ktor.clientIos)
                implementation(Deps.JetBrains.Coroutines.core) {
                    version {
                        strictly(Deps.JetBrains.Coroutines.VERSION)
                    }
                }
            }
        }
        val iosSimulatorArm64Main by getting {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.nativeDriver)
                implementation(Deps.JetBrains.Ktor.clientIos)
                implementation(Deps.JetBrains.Coroutines.core) {
                    version {
                        strictly(Deps.JetBrains.Coroutines.VERSION)
                    }
                }
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

    }

}

sqldelight {
    database("SuprSendDatabase") { // This will be the name of the generated database class.
        packageName = Deps.SDK_PACKAGE_NAME
        schemaOutputDirectory = file("src/commonMain/sqldelight/databases")
        verifyMigrations = true
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("jitpack") {
                // Configure the publication here
                artifactId = Deps.Publication.ARTIFACT_ID
                group = Deps.Publication.GROUP
                version = Deps.Publication.VERSION
                from(components["release"])
            }
            repositories {
                maven {
                    url = uri("https://jitpack.io")
                    credentials {
                        username = Deps.JITPACK_TOKEN
                    }
                }
            }
        }
    }
}


android {
    compileSdkVersion(Deps.Android.compileSdk)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(Deps.Android.minSdk)
        targetSdkVersion(Deps.Android.targetSdk)
        consumerProguardFiles("consumer-rules.pro")
        multiDexEnabled = true
//        versionCode = Deps.SDK_VERSION_CODE
//        versionName = Deps.SDK_VERSION_NAME
        buildConfigField("String", "SS_SDK_VERSION_CODE", "\"${Deps.SDK_VERSION_CODE}\"")
        buildConfigField("String", "SS_SDK_VERSION_NAME", "\"${Deps.SDK_VERSION_NAME}\"")
    }
}
