plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}
apply {
    from("$rootDir/ktlint.gradle")
}
android {

    compileSdkVersion(Deps.Android.compileSdk)
    buildToolsVersion(Deps.Android.buildToolsVersion)

    defaultConfig {
        applicationId = "app.suprsend.android.android"
        minSdkVersion(Deps.Android.minSdk)
        targetSdkVersion(Deps.Android.targetSdk)
        versionCode = Deps.SDK_VERSION_CODE
        versionName = Deps.SDK_VERSION_NAME
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = "(d)"
            isDebuggable = true
            isCrunchPngs = false
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
//    packagingOptions {
//        exclude("META-INF/ktor-client-core.kotlin_module")
//        exclude("META-INF/ktor-io.kotlin_module")
//        exclude("META-INF/ktor-http.kotlin_module")
//        exclude("META-INF/ktor-http-cio.kotlin_module")
//        exclude("META-INF/ktor-utils.kotlin_module")
//        exclude("META-INF/ktor-client-serialization.kotlin_module")
//        exclude("META-INF/ktor-client-logging.kotlin_module")
//        exclude("META-INF/ktor-client-json.kotlin_module")
//        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
//    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.JetBrains.Kotlin.VERSION}")
    implementation(Deps.CORE_KTX)
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("com.google.firebase:firebase-crashlytics:18.2.0")
//    implementation(project(":shared"))
    implementation("com.github.suprsend:suprsend-android-sdk:0.0.0.2")
    implementation("com.mixpanel.android:mixpanel-android:5.9.1")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}