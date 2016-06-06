# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontoptimize
-dontpreverify
-dontwarn cn.sharesdk.**
-keep class cn.sharesdk.** { *;}
-dontwarn com.mob.**
-keep class com.mob.**{*;}
-dontwarn **.R$*
-keep class **.R$* {*;}
-keep class **.R{*;}

-dontwarn com.alipay.**
-keep class com.alipay.** { *;}
-dontwarn com.sina.**
-keep class com.sina.** { *;}
-dontwarn com.taobao.**
-keep class com.taobao.** { *;}
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}

# jpush
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }
-keep class org.apache.commons.logging.**

#-keep com.huotu.partnermall.inner.R$*{ *;}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# butterknife
-dontwarn butterknife.internal.**
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.huotu.partnermall.model.** { *; }

-dontwarn com.google.**
-keep class com.google.gson.** {*;}

# Application classes that will be serialized/deserialized over Gson

##---------------End: proguard configuration for Gson  ----------


#----------eventbus--------------------
-keep class de.greenrobot.event.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}