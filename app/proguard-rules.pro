# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sdk-app-shy/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn com.tencent.bugly.**
-dontwarn com.facebook.**
-dontwarn com.ldoublem.**
-dontwarn com.mistyrain.**
-dontwarn com.alibaba.**
-dontwarn javax.**
-dontwarn android.**
-dontwarn com.squareup.**
-dontwarn com.google.**
-dontwarn java.**
-dontwarn cn.ihuoniao.model.**
-keep class javax.**{*;}
-keep class android.**{*;}
-keep public class com.tencent.bugly.**{*;}
-keep public class com.facebook.**{*;}
-keep public class com.ldoublem.**{*;}
-keep public class com.mistyrain.**{*;}
-keep class java.**{*;}
-keep class cn.ihuoniao.model.**{*;}
-keep class com.squareup.**{*;}
-keep class com.google.**{*;}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
-keep class com.alibaba.fastjson.** { *; }
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-dontusemixedcaseclassnames
    -dontshrink
    -dontoptimize
    -dontwarn com.google.android.maps.**
    -dontwarn android.webkit.WebView
    -dontwarn com.umeng.**
    -dontwarn com.tencent.weibo.sdk.**
    -dontwarn com.facebook.**
    -keep public class javax.**
    -keep public class android.webkit.**
    -dontwarn android.support.v4.**
    -keep enum com.facebook.**
    -keepattributes Exceptions,InnerClasses,Signature
    -keepattributes *Annotation*
    -keepattributes SourceFile,LineNumberTable

    -keep public interface com.facebook.**
    -keep public interface com.tencent.**
    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**
    -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
    -keep public class com.umeng.socialize.* {*;}


    -keep class com.facebook.**
    -keep class com.facebook.** { *; }
    -keep class com.umeng.scrshot.**
    -keep public class com.tencent.** {*;}
    -keep class com.umeng.socialize.sensor.**
    -keep class com.umeng.socialize.handler.**
    -keep class com.umeng.socialize.handler.*
    -keep class com.umeng.weixin.handler.**
    -keep class com.umeng.weixin.handler.*
    -keep class com.umeng.qq.handler.**
    -keep class com.umeng.qq.handler.*
    -keep class UMMoreHandler{*;}
    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
    -keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
    -keep class im.yixin.sdk.api.YXMessage {*;}
    -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
    -keep class com.tencent.mm.sdk.** {
     *;
    }
    -keep class com.tencent.mm.opensdk.** {
   *;
    }
    -dontwarn twitter4j.**
    -keep class twitter4j.** { *; }

    -keep class com.tencent.** {*;}
    -dontwarn com.tencent.**
    -keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
    }
    -keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
        }
    -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

    -keep class com.tencent.open.TDialog$*
    -keep class com.tencent.open.TDialog$* {*;}
    -keep class com.tencent.open.PKDialog
    -keep class com.tencent.open.PKDialog {*;}
    -keep class com.tencent.open.PKDialog$*
    -keep class com.tencent.open.PKDialog$* {*;}

    -keep class com.sina.** {*;}
    -dontwarn com.sina.**
    -keep class  com.alipay.share.sdk.** {
       *;
    }
    -keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }

    -keep class com.linkedin.** { *; }
    -keepattributes Signature