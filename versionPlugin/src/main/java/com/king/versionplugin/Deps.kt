package com.king.versionplugin

@Suppress("unused")
object Basic {
    const val applicationId = "com.king.app.workhelper"
    const val compileSdkVersion = 31
    const val buildToolsVersion = "31.0.0"
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
    const val versionCode = 1
    const val versionName = "1.0.0"
}

@Suppress("unused")
object TestDep {
    const val junit = "junit:junit:4.13.2"
}

@Suppress("unused")
object AndroidXDep {
    const val android_junit = "androidx.test.ext:junit:1.1.5"
    const val espresso_core = "androidx.test.espresso:espresso-core:3.4.0"

    const val appcompat = "androidx.appcompat:appcompat:1.3.1"

    //const val fragment = "androidx.fragment:fragment:1.3.0"
    //const val annotation = "androidx.annotation:annotation:1.2.0"
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.2"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0"
    const val palette = "androidx.palette:palette:1.0.0"

    private const val lifecycle_version = "2.3.0"
    const val lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
    const val lifecycle_common_java8 =
        "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
    const val lifecycle_process = "androidx.lifecycle:lifecycle-process:${lifecycle_version}"

    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    const val webkit = "androidx.webkit:webkit:1.4.0"

    private const val work_version = "2.8.0"

    //Java only
    const val work_runtime = "androidx.work:work-runtime:$work_version"

    // Kotlin + coroutines
    const val work_runtime_ktx = "androidx.work:work-runtime-ktx:$work_version"

    // optional - RxJava2 support
    const val work_rxjava2 = "androidx.work:work-rxjava2:$work_version"

    // optional - GCMNetworkManager support
    const val work_gcm = "androidx.work:work-gcm:$work_version"

    // optional - Test helpers
    const val work_testing = "androidx.work:work-testing:$work_version"

    // optional - Multiprocess support
    const val work_multiprocess = "androidx.work:work-multiprocess:$work_version"
}

@Suppress("unused")
object KotlinDep {
    // 1.7.0 要求 minCompileSdk=31
    const val core_ktx = "androidx.core:core-ktx:1.6.0"

    //Kotlin 为 JVM 平台提供的标准库
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:1.6.20"

    //Kotlin JDK 8 上的扩展库
    const val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.20"

    //Kotlin 协程在 Android 平台的支持库
    const val kotlinx_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1"

    const val kotlinx_coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1"

    //1.4.0 版本要求 sdk-31
    const val activity_ktx = "androidx.activity:activity-ktx:1.3.1"

    //1.4.0 版本要求 sdk-31
    const val fragment_ktx = "androidx.fragment:fragment-ktx:1.3.6"

    //2.4.1 版本要求 sdk-31
    const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
}

@Suppress("unused")
object GoogleDep {
    const val design = "com.google.android.material:material:1.3.0"

    const val gson = "com.google.code.gson:gson:2.8.5"
    const val flexbox = "com.google.android:flexbox:1.0.0"
    const val auto_service = "com.google.auto.service:auto-service:1.0"

    const val firebase_analytics = "com.google.firebase:firebase-analytics"
    const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics"
    const val firebase_bom = "com.google.firebase:firebase-bom:28.1.0"
}

@Suppress("unused")
object ThirdDep {
    const val okhttp = "com.squareup.okhttp3:okhttp:4.9.0"

    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val adapter_rxjava2 = "com.squareup.retrofit2:adapter-rxjava2:2.9.0"

    const val javapoet = "com.squareup:javapoet:1.11.1"

    const val fastjson = "com.alibaba:fastjson:1.1.71.android"
    const val permissionsdispatcher = "org.permissionsdispatcher:permissionsdispatcher:3.3.2"
    const val permissionsdispatcher_processor =
        "org.permissionsdispatcher:permissionsdispatcher-processor:3.3.2"
    const val rxpermissions = "com.github.tbruyelle:rxpermissions:0.10.2"
    const val easypermissions = "pub.devrel:easypermissions:2.0.1"

    // 图片压缩，https://github.com/Curzibn/Luban
    const val luban = "top.zibin:Luban:1.1.8"
    const val otto = "com.squareup:otto:1.3.8"
    const val eventbus = "org.greenrobot:eventbus:3.1.1"

    const val butterknife = "com.jakewharton:butterknife:10.2.1"
    const val butterknife_compiler = "com.jakewharton:butterknife-compiler:10.2.1"

    const val rxjava = "io.reactivex.rxjava2:rxjava:2.2.17"
    const val rxandroid = "io.reactivex.rxjava2:rxandroid:2.0.1"
    const val rxbinding = "com.jakewharton.rxbinding2:rxbinding:2.0.0"

    const val rxlifecycle = "com.trello.rxlifecycle3:rxlifecycle:3.0.0"
    const val rxlife = "com.rxjava.rxlife:rxlife:1.0.9"

    const val leakcanary_android = "com.squareup.leakcanary:leakcanary-android:2.7"

    const val blockcanary_android = "com.github.markzhai:blockcanary-android:1.5.0"
    const val blockcanary_no_op = "com.github.markzhai:blockcanary-no-op:1.5.0"

    const val glide = "com.github.bumptech.glide:glide:4.12.0"
    const val glide_compiler = "com.github.bumptech.glide:compiler:4.12.0"
    const val glide_okhttp3_integration = "com.github.bumptech.glide:okhttp3-integration:4.12.0"

    const val fresco = "com.facebook.fresco:fresco:1.12.1"
    const val imagepipeline_okhttp3 = "com.facebook.fresco:imagepipeline-okhttp3:1.12.1"

    const val stetho = "com.facebook.stetho:stetho:1.5.0"
    const val stetho_okhttp3 = "com.facebook.stetho:stetho-okhttp3:1.5.0"

    const val soloader = "com.facebook.soloader:soloader:0.10.3"
    const val flipper = "com.facebook.flipper:flipper:0.121.1"
    const val flipper_noop = "com.facebook.flipper:flipper-noop:0.121.1"

    // Facebook 开源的声明式 UI 渲染框架。https://fblitho.com/
    const val litho_core = "com.facebook.litho:litho-core:0.47.0"
    const val litho_core_kotlin = "com.facebook.litho:litho-core-kotlin:0.47.0"
    const val litho_widget = "com.facebook.litho:litho-widget:0.47.0"
    const val litho_widget_kotlin = "com.facebook.litho:litho-widget-kotlin:0.47.0"
    const val litho_processor = "com.facebook.litho:litho-processor:0.47.0"

    const val flatbuffers_java = "com.google.flatbuffers:flatbuffers-java:1.8.0"

    const val dagger_android = "com.google.dagger:dagger-android:2.28.1"
    const val dagger_compiler = "com.google.dagger:dagger-compiler:2.28.1"

    const val hilt_android = "com.google.dagger:hilt-android:2.41"
    const val hilt_android_compiler = "com.google.dagger:hilt-android-compiler:2.41"

    const val arouter_api = "com.alibaba:arouter-api:1.5.2"
    const val arouter_compiler = "com.alibaba:arouter-compiler:1.5.2"

    const val calendarview = "com.haibin:calendarview:3.6.6"

    // 扫描+生成二维码
    const val bga_qrcode_zxing = "cn.bingoogolapple:bga-qrcode-zxing:1.3.6"

    //扫描
    const val bga_qrcode_zbar = "cn.bingoogolapple:bga-qrcode-zbar:1.3.6"

    const val doraemonkit = "com.didichuxing.doraemonkit:doraemonkit:2.2.1"
    const val doraemonkit_no_op = "com.didichuxing.doraemonkit:doraemonkit-no-op:2.2.1"

    //NPE检测工具 https://github.com/uber/NullAway
    const val nullaway = "com.uber.nullaway:nullaway:0.2.1"

    const val SmartRefreshLayout = "com.scwang.smartrefresh:SmartRefreshLayout:1.1.0-alpha-14"
    const val SmartRefreshHeader = "com.scwang.smartrefresh:SmartRefreshHeader:1.1.0-alpha-14"

    // https://bugly.qq.com/docs/release-notes/release-android-bugly/
    // "com.tencent.bugly:nativecrashreport:3.9.2" 是 so 库依赖，crashreport:4.0.4 以下需要引入，>= 4.0.4 合并了
    const val Bugly = "com.tencent.bugly:crashreport:4.0.4"

    const val disklrucache = "com.jakewharton:disklrucache:2.0.2"
}