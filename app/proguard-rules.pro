# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
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

################################ 下列类不能进行混淆 ########################################
#
#(1)、反射用到的类
#(2)、在AndroidManifest中配置的类(Activity、Service等的子类及Framework类默认不会进行混淆)
#(3)、Jni中调用的类
#
###########################################################################################

################################ ProGuard的常用语法 ########################################
#
#-libraryjars class_path 应用的依赖包，如android-support-v4
#-keep [,modifier,...] class_specification 不混淆某些类
#-keepclassmembers [,modifier,...] class_specification 不混淆类的成员
#-keepclasseswithmembers [,modifier,...] class_specification 不混淆类及其成员
#-keepnames class_specification 不混淆类及其成员名
#-keepclassmembernames class_specification 不混淆类的成员名
#-keepclasseswithmembernames class_specification 不混淆类及其成员名
#-assumenosideeffects class_specification 假设调用不产生任何影响，在proguard代码优化时会将该调用remove掉。如system.out.println和Log.v等等
#-dontwarn [class_filter] 不提示warnning
#
#see more: http://proguard.sourceforge.net/index.html#manual/usage.html
###########################################################################################

#关闭混淆
#-dontobfuscate

############################## 反射 ###############################
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes EnclosingMethod

-keep class com.king.app.workhelper.model.** { *; }

#release模式下移除Log代码
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** v(...);
#}

########################################################################
#                                 系统类
########################################################################
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

############################## Serializable ###############################
-keep class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


########################################################################
#                                 第三方类
########################################################################

############################## 自定义注解 ###############################
-keep @com.king.applib.annotation.DoNotProguard class * {*;}
-keep,allowobfuscation @interface com.king.applib.annotation.DoNotProguard
-keepclassmembers class * { @com.king.applib.annotation.DoNotProguard *; }

############################## butterknife ###############################
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep public class * implements butterknife.Unbinder { public <init>(...); }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-dontwarn butterknife.internal.**

############################## OkHttp ###############################
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

############################## Square Otto ##########################
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

############################## RxJava ###############################
-dontwarn rx.internal.util.unsafe.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

############################## retrofit2 ###############################
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

############################## okhttp3 ###############################
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

############################## fresco ###############################
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

############################## okio ###############################
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

############################## stetho ###############################
# Note: Doesn't include Javascript console lines. See https://github.com/facebook/stetho/tree/master/stetho-js-rhino#proguard
-keep class com.facebook.stetho.** { *; }

############################## gson ###############################
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }