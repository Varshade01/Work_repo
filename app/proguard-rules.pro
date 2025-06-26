# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# --- Compose (best practice) ---
-keep class androidx.compose.** { *; }
-keep class androidx.activity.ComponentActivity { *; }
-dontwarn androidx.compose.**

# --- Hilt (best practice) ---
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# --- Retrofit & Moshi (best practice) ---
-keep class retrofit2.** { *; }
-keep class com.squareup.moshi.** { *; }
-dontwarn retrofit2.**
-dontwarn com.squareup.moshi.**

# --- WebView JS interface (if used) ---
#-keepclassmembers class com.maat.cha.** {
#   @android.webkit.JavascriptInterface <methods>;
#}

# --- Keep mapping for crash reporting ---
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*

# --- General Android best practice ---
-keep class * extends android.app.Application { *; }
-keep class * extends android.app.Activity { *; }
-keep class * extends android.app.Service { *; }
-keep class * extends android.content.BroadcastReceiver { *; }
-keep class * extends android.content.ContentProvider { *; }
-keep public class * extends android.view.View { public <init>(android.content.Context); public <init>(android.content.Context, android.util.AttributeSet); }
-keepclassmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-dontwarn org.jetbrains.annotations.**