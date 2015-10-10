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
-dontwarn cn.share.**
-keep class cn.share.** { *;}
-dontwarn com.alipay.**
-keep class com.alipay.** { *;}
-dontwarn com.sina.**
-keep class com.sina.** { *;}
-dontwarn com.taobao.**
-keep class com.taobao.** { *;}
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
