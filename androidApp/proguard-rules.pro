-dontwarn app.suprsend.**
-keep class app.suprsend.**{*;}


-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Change here app.suprsend with your app package
-keep,includedescriptorclasses class app.suprsend.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class app.suprsend.** { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class app.suprsend.** { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}