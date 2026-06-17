-keep class com.moonshot.kimiclaw.** { *; }
-keep class com.termux.** { *; }
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
