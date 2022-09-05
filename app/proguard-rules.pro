-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontshrink
-dontoptimize
-dontpreverify
-keepattributes *Annotation*
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.*
-keep public class com.ideaheap.io.* { *; }

-keepclassmembers class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class android.support.** { *; }
-dontwarn android.support.**

-keep class com.google.android.** { *; }
-dontwarn com.google.android.**

-keep class android.net.http.** { *; }
-dontwarn android.net.http.**

-keep class org.apache.** { *; }
-dontwarn org.apache.**
-keepclassmembers public class org.apache.http.** { *; }

-keep class com.android.vending.billing.**

-keepattributes Signature

-keep public class javax.net.ssl.**
-keepclassmembers public class javax.net.ssl.** { *; }

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }

-keep class com.google.common.** { *; }
-dontwarn com.google.common.**

-dontwarn okhttp3.**
-dontwarn okio.**

-keep class me.dm7.barcodescanner.** { *; }

-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}

-keep class android.os.Build {
    java.lang.String[] SUPPORTED_ABIS;
    java.lang.String CPU_ABI;
}
-keep class android.content.res.Configuration {
    android.os.LocaleList getLocales();
    java.util.Locale locale;
}
-keep public class com.android.installreferrer.** { *; }

-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**
-dontwarn com.google.errorprone.**
-dontwarn sun.misc.Unsafe
-dontwarn java.lang.ClassValue

-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler{}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
 }

 # Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
 # EnclosingMethod is required to use InnerClasses.
 -keepattributes Signature, InnerClasses, EnclosingMethod

 # Retrofit does reflection on method and parameter annotations.
 -keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

 # Keep annotation default values (e.g., retrofit2.http.Field.encoded).
 -keepattributes AnnotationDefault

 # Retain service method parameters when optimizing.
 -keepclassmembers,allowshrinking,allowobfuscation interface * {
     @retrofit2.http.* <methods>;
 }

 # Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
 -dontwarn kotlin.Unit

 # Top-level functions that can only be used by Kotlin.
 -dontwarn retrofit2.KotlinExtensions
 -dontwarn retrofit2.KotlinExtensions$*

 # With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
 # and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
 -if interface * { @retrofit2.http.* <methods>; }
 -keep,allowobfuscation interface <1>

 # Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

 # Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
 -dontwarn org.codehaus.mojo.animal_sniffer.*

 # When editing this file, update the following files as well:
 # - META-INF/com.android.tools/proguard/coroutines.pro
 # - META-INF/com.android.tools/r8/coroutines.pro

 # ServiceLoader support
 -keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
 -keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

 # Most of volatile fields are updated with AFU and should not be mangled
 -keepclassmembers class kotlinx.coroutines.** {
     volatile <fields>;
 }

 # Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
 -keepclassmembers class kotlin.coroutines.SafeContinuation {
     volatile <fields>;
 }

 # These classes are only required by kotlinx.coroutines.debug.AgentPremain, which is only loaded when
 # kotlinx-coroutines-core is used as a Java agent, so these are not needed in contexts where ProGuard is used.
 -dontwarn java.lang.instrument.ClassFileTransformer
 -dontwarn sun.misc.SignalHandler
 -dontwarn java.lang.instrument.Instrumentation
 -dontwarn sun.misc.Signal

 # Only used in `kotlinx.coroutines.internal.ExceptionsConstructor`.
 # The case when it is not available is hidden in a `try`-`catch`, as well as a check for Android.
 -dontwarn java.lang.ClassValue

 # An annotation used for build tooling, won't be directly accessed.
 -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

 -dontwarn org.jetbrains.annotations.**

 -keep class * extends androidx.fragment.app.Fragment{}
 -keepnames class androidx.navigation.fragment.NavHostFragment
 -keepnames class * extends android.os.Parcelable
 -keepnames class * extends java.io.Serializable
