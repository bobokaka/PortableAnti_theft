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
#百度地图混淆规则
-keep class com.baidu.** {*;}
                                 -keep class mapsdkvi.com.** {*;}
                                 -dontwarn com.baidu.**
                                 -keep class butterknife.** { *; }
                                 -dontwarn butterknife.internal.**
                                 -keep class **$$ViewBinder { *; }
#butterknife混淆规则
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#okhttp混淆规则
# JSR 305 annotations are for embedding nullability information.
#JSR 305注释用于嵌入可空性信息。
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
#资源是用相对路径加载的，因此必须保留此类的包。
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
#仅通过动物嗅探器来依赖API，以确保API与旧版本的Java兼容。
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
#OKHTTP平台仅在JVM上使用，并且当可以使用同源密码依赖项时。
-dontwarn okhttp3.internal.platform.ConscryptPlatform


#Mob短信验证混淆规则规避
-keep class com.mob.**{*;}
-keep class cn.smssdk.**{*;}
-dontwarn com.mob.**