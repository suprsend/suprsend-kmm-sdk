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
                baseName = "shared"
                isStatic = true
                embedBitcode(type)
                freeCompilerArgs = listOf("-Xadd-light-debug=enable")
            }
        }
    }
    if (onSimulator) {
        iosArm64("ios") {
            mavenPublication {
                artifactId = "${project.name}-iosArm64"
            }
        }
    } else {
        iosX64("ios") {
            mavenPublication {
                artifactId = "${project.name}-iosx64"
            }
        }
    }

    //macosX64
    //macosArm64
    //iosArm32 - iOS ARM 32
    //iosArm64 - iOS ARM 64
    //iosX64 - iOS Simulator (x86_64)


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
                implementation("com.soywiz.korlibs.krypto:krypto:2.2.0")
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
                implementation(Deps.AndroidX.CORE_KTX)

                api("com.google.firebase:firebase-messaging:22.0.0")

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
        val iosMain by getting {
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
        val iosTest by getting
    }


    tasks.register<org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask>("debugFatFramework") {
        baseName = "shared"
        destinationDir = buildDir.resolve("xcode-frameworks")
        from(
//            kotlin.targets.getByName<KotlinNativeTarget>("iosArm64").binaries.getFramework("DEBUG"),
            kotlin.targets.getByName<KotlinNativeTarget>("iosX64").binaries.getFramework("DEBUG")
        )
    }

    tasks.register<org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask>("releaseFatFramework") {
        baseName = "shared"
        destinationDir = buildDir.resolve("xcode-frameworks")
        from(
            kotlin.targets.getByName<KotlinNativeTarget>("iosArm64").binaries.getFramework("RELEASE"),
//            kotlin.targets.getByName<KotlinNativeTarget>("iosX64").binaries.getFramework("RELEASE"),
        )
    }
}

sqldelight {
    database("SuprSendDatabase") { // This will be the name of the generated database class.
        packageName = Deps.SDK_PACKAGE_NAME
        schemaOutputDirectory = file("src/commonMain/sqldelight/databases")
        verifyMigrations = true
    }
}

fun getIosTarget(): String {
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    return if (sdkName.startsWith("iphoneos")) "iosArm64" else "iosX64"
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
        versionCode = Deps.SDK_VERSION_CODE
        versionName = Deps.SDK_VERSION_NAME
        buildConfigField("String", "SS_SDK_VERSION_CODE", "\"${versionCode.toString()}\"")
        buildConfigField("String", "SS_SDK_VERSION_NAME", "\"$versionName\"")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val targetName = getIosTarget()
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from(framework.outputDirectory)
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)