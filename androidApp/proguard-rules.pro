# SuprSend Sdk
-dontwarn app.suprsend.**
-keep class app.suprsend.**{*;}

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Change "app.suprsend.android" with your app package name
-keep,includedescriptorclasses class app.suprsend.android.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class app.suprsend.android.** { # <-- Change "app.suprsend.android" with your app package name
    *** Companion;
}
-keepclasseswithmembers class app.suprsend.android.** { # <-- Change "app.suprsend.android" with your app package name
    kotlinx.serialization.KSerializer serializer(...);
}