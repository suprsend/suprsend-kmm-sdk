import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
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

    val onSimulator = System.getenv("SDK_NAME")?.startsWith("iphonesimulator") ?: false
    val type = if (onSimulator) {
        "marker"
    } else {
        "bitcode"
    }
    ios()
    ios {
        binaries {
            framework {
                isStatic = true
                embedBitcode(type)
                freeCompilerArgs = listOf("-Xadd-light-debug=enable")
            }
        }
    }


    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-progressive")
        }
    }
    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.RequiresOptIn")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

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
                //Todo version constants
            }
        }
        val commonTest by getting {
            dependencies {
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
                implementation(Deps.CORE_KTX)

                api("com.google.firebase:firebase-analytics:19.0.0")
                api("com.google.firebase:firebase-messaging:22.0.0")

                //implementation("com.google.code.gson:gson:2.8.6")

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
                implementation("io.mockk:mockk:1.12.0")
                implementation(Deps.Squareup.SQLDelight.sqliteDriver)
            }
        }
        val iosMain by getting {
        }
        val iosTest by getting
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
                        username = project.property("authToken").toString()
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
        versionCode = Deps.SDK_VERSION_CODE
        versionName = Deps.SDK_VERSION_NAME
        buildConfigField("String", "SS_SDK_VERSION_CODE", "\"${versionCode.toString()}\"")
        buildConfigField("String", "SS_SDK_VERSION_NAME", "\"$versionName\"")
    }
}