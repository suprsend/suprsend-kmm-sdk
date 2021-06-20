object Deps {

    object Touchlab {
        const val stately = "co.touchlab:stately-common:1.1.0"
        const val kermit = "co.touchlab:kermit:0.1.8"
        const val karmok = "co.touchlab:karmok-library:0.1.8"
        const val crashkios = "co.touchlab:crashkios:0.3.0"
    }

    object AndroidX {
        object AppCompat {
            const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        }

        object Test {
            private const val androidxTest = "1.2.0"
            private const val androidxTestExt = "1.1.1"
            const val core = "androidx.test:core:$androidxTest"
            const val junit = "androidx.test.ext:junit:$androidxTestExt"
            const val runner = "androidx.test:runner:$androidxTest"
            const val rules = "androidx.test:rules:$androidxTest"
        }
    }

    object Android {
        const val minSdk = 21
        const val targetSdk = 30
        const val compileSdk = 30

        object Tools {
            object Build {
                const val gradlePlugin = "com.android.tools.build:gradle:4.1.1"
            }
        }
    }

    object Squareup {
        const val mockServer = "com.squareup.okhttp3:mockwebserver:4.9.0"

        object SQLDelight {
            private const val VERSION = "1.4.4"
            const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$VERSION"
            const val androidDriver = "com.squareup.sqldelight:android-driver:$VERSION"
            const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:$VERSION"
            const val nativeDriver = "com.squareup.sqldelight:native-driver:$VERSION"
            const val coroutinesExtension =
                "com.squareup.sqldelight:coroutines-extensions:$VERSION"
        }
    }

    object Apollo {
        private const val VERSION = "2.4.6"
        const val gradlePlugin = "com.apollographql.apollo:apollo-gradle-plugin:$VERSION"
        const val api = "com.apollographql.apollo:apollo-api:$VERSION"
        const val kotlinRuntime = "com.apollographql.apollo:apollo-runtime-kotlin:$VERSION"
        const val coroutinesSupport =
            "com.apollographql.apollo:apollo-coroutines-support:$VERSION"
    }

    object JetBrains {
        object Kotlin {
            // __KOTLIN_COMPOSE_VERSION__
            const val VERSION = "1.4.10"
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
            const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"
            const val testCommon = "org.jetbrains.kotlin:kotlin-test-common:$VERSION"
            const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
            const val testAnnotationsCommon =
                "org.jetbrains.kotlin:kotlin-test-annotations-common:$VERSION"
            const val testKotlin = "org.jetbrains.kotlin:kotlin-test:$VERSION"
        }

        object Kotlinx {
            private val ATOMICFU_VERSION = "0.15.1"
//            val atomicfuCommon = "org.jetbrains.kotlinx:atomicfu-common:$ATOMICFU_VERSION"
        }

        object Compose {
            // __LATEST_COMPOSE_RELEASE_VERSION__
            private const val VERSION = "0.3.0-build135"
            const val gradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:$VERSION"
        }

        object Ktor {
            private const val VERSION = "1.5.0"
            const val clientCommon = "io.ktor:ktor-client-core:$VERSION"
            const val clientAndroid = "io.ktor:ktor-client-okhttp:$VERSION"
            const val clientIos = "io.ktor:ktor-client-ios:$VERSION"

            const val serialization = "io.ktor:ktor-client-serialization:$VERSION"
            const val commonLogging = "io.ktor:ktor-client-logging:$VERSION"


            const val clientMock = "io.ktor:ktor-client-mock:$VERSION"
        }

        object Coroutines {
            const val VERSION = "1.4.2-native-mt"
            const val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION"
        }
    }
}