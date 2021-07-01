plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

android {

    compileSdkVersion(Deps.Android.compileSdk)
    defaultConfig {
        applicationId = "app.suprsend.android.android"
        minSdkVersion(Deps.Android.minSdk)
        targetSdkVersion(Deps.Android.targetSdk)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.jks")
            storePassword = "debugdebug"
            keyAlias = "debug"
            keyPassword = "debugdebug"
        }
        create("release") {
            storeFile = file("../debug.jks")
            storePassword = "debugdebug"
            keyAlias = "debug"
            keyPassword = "debugdebug"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
    }

    packagingOptions {
        exclude("META-INF/ktor-client-core.kotlin_module")
        exclude("META-INF/ktor-io.kotlin_module")
        exclude("META-INF/ktor-http.kotlin_module")
        exclude("META-INF/ktor-http-cio.kotlin_module")
        exclude("META-INF/ktor-utils.kotlin_module")
        exclude("META-INF/ktor-client-serialization.kotlin_module")
        exclude("META-INF/ktor-client-logging.kotlin_module")
        exclude("META-INF/ktor-client-json.kotlin_module")
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}

dependencies {

    implementation(project(":shared"))

    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    val coroutinesAndroidVersion = "1.3.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesAndroidVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesAndroidVersion")
    implementation("androidx.multidex:multidex:2.0.1")

    //Todo: Figureout bom version support in shared lib
    //If you are changing bom version please change shared module analytics version manually bom is not supported there
    implementation(platform("com.google.firebase:firebase-bom:28.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")

    implementation("com.google.code.gson:gson:2.8.6")
}