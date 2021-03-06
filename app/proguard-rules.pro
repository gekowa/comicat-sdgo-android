# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-studio\sdk/tools/proguard/proguard-android.txt
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

-keep class cn.sdgundam.comicatsdgo.api_model.UnitInfo { *; }
-keep class cn.sdgundam.comicatsdgo.VideoViewActivity$YoukuJSInterface { *; }
-keep class cn.sdgundam.comicatsdgo.VideoViewActivity$VideoInfoInterface { *; }
-keep class cn.sdgundam.comicatsdgo.R.integer { *** interstitial_ads_freq_*; }
-keep class cn.sdgundam.comicatsdgo.InterstitialAdsManager { void displayAdsOf*(...); }

-keep public class io.vov.vitamio.** { *; }

# 广点通
-keep class com.qq.e.** {
    public protected *;
}
-keep class com.tencent.gdt.**{
    public protected *;
}

# Ignore warnings: https://github.com/square/okhttp/wiki/FAQs
-dontwarn com.squareup.okhttp.internal.huc.**
# Ignore warnings: https://github.com/square/okio/issues/60
-dontwarn okio.**

# 有米
-dontwarn net.youmi.android.**
-keep class net.youmi.android.** { *; }

# SnappyDB
-keep class com.snappydb.** { *; }
-dontwarn com.esotericsoftware.kryo.**
-keep class com.esotericsoftware.kryo.** { *; }
-dontwarn org.objenesis.instantiator.**

# 多盟积分墙
-dontwarn cn.dm.**
-keep class cn.dm.** { *; }
-keep class cn.dm.android.ui.interaction.** {*;}
-keepattributes *Annotation*

# 友盟自动更新
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class com.tencent.** { *; }
-dontwarn com.tencent.**

-keep class com.umeng.** { *; }
-dontwarn com.umeng.**