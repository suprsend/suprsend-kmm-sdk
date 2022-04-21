object Deps {

    const val MAJOR_VERSION = 0
    const val MINOR_VERSION = 1
    const val BETA = 3
    const val APP_VERSION_CODE = 2
    const val ISPROD = false

    var APP_VERSION_NAME = if (ISPROD) {
        "$MAJOR_VERSION.$MINOR_VERSION Prod Beta $BETA"
    } else {
        "$MAJOR_VERSION.$MINOR_VERSION Stag Beta $BETA"
    }

    const val RUN_LIB = true

    const val SDK_PACKAGE_NAME = "app.suprsend"
    const val SDK_VERSION_CODE = 1
    const val SDK_VERSION_NAME = "$MAJOR_VERSION.${MINOR_VERSION}Beta$BETA"

    var SS_API_BASE_URL = if (ISPROD) "https://hub.suprsend.com" else "https://collector-staging.suprsend.workers.dev"
    var SS_TOKEN = "XXXX"
    var SS_SECRET = "XXXX"

    const val XIAOMI_APP_ID = "XXXX"
    const val XIAOMI_APP_KEY = "XXXX"

    const val MX_TOKEN = "XXXX"
    const val JITPACK_TOKEN = "XXXX"

    object Android {
        const val minSdk = 19
        const val targetSdk = 30
        const val compileSdk = 30
        const val buildToolsVersion = "30.0.3"
    }

    object Publication {
        const val ARTIFACT_ID = "suprsend-kmm-sdk"
        const val GROUP = "com.github.suprsend"
        const val VERSION = "$MAJOR_VERSION.${MINOR_VERSION}Beta$BETA"
    }

    object AndroidX {
        const val ANNOTATION = "androidx.annotation:annotation:1.2.0"
        const val CORE_KTX = "androidx.core:core-ktx:1.6.0"

        //Test
        private const val androidxTest = "1.2.0"
        private const val androidxTestExt = "1.1.1"
        const val core = "androidx.test:core:$androidxTest"
        const val junit = "androidx.test.ext:junit:$androidxTestExt"
        const val runner = "androidx.test:runner:$androidxTest"
        const val rules = "androidx.test:rules:$androidxTest"
    }


    object Squareup {
        const val mockServer = "com.squareup.okhttp3:mockwebserver:4.9.0"

        object SQLDelight {
            private const val VERSION = "1.5.3"
            const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$VERSION"
            const val androidDriver = "com.squareup.sqldelight:android-driver:$VERSION"
            const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:$VERSION"
            const val nativeDriver = "com.squareup.sqldelight:native-driver:$VERSION"
            const val coroutinesExtension = "com.squareup.sqldelight:coroutines-extensions:$VERSION"
        }
    }

    object JetBrains {
        object Kotlin {
            const val VERSION = "1.6.20-M1"
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
            const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2"
            const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.3.2"
            const val testCommon = "org.jetbrains.kotlin:kotlin-test-common:$VERSION"
            const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
            const val testAnnotationsCommon = "org.jetbrains.kotlin:kotlin-test-annotations-common:$VERSION"
            const val testKotlin = "org.jetbrains.kotlin:kotlin-test:$VERSION"
        }


        object Ktor {
            private const val VERSION = "2.0.0"
            const val clientCommon = "io.ktor:ktor-client-core:$VERSION"
            const val clientAndroid = "io.ktor:ktor-client-okhttp:$VERSION"
            const val clientIos = "io.ktor:ktor-client-ios:$VERSION"

            const val serialization = "io.ktor:ktor-client-serialization:$VERSION"
            const val commonLogging = "io.ktor:ktor-client-logging:$VERSION"

            const val clientMock = "io.ktor:ktor-client-mock:$VERSION"
        }

        object Coroutines {
            const val VERSION = "1.6.1"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION"
        }
    }
}