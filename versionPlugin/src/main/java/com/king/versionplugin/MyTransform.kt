package com.king.versionplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager

/**
 * https://rebooters.github.io/2020/01/04/Gradle-Transform-ASM-%E6%8E%A2%E7%B4%A2/
 *
 * @author VanceKing
 * created at 2023/3/28
 */
class MyTransform : Transform() {
    override fun getName(): String = NAME

    /*
    需要处理的数据类型，有两种枚举类型: CLASSES 代表处理的 java 的 class 文件，RESOURCES 代表要处理 java 的资源
     */
    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
        TransformManager.CONTENT_CLASS

    /*
     指 Transform 要操作内容的范围，官方文档 Scope 有 7 种类型：
     1. EXTERNAL_LIBRARIES        只有外部库
     2. PROJECT                   只有项目内容
     3. PROJECT_LOCAL_DEPS        只有项目的本地依赖(本地jar)
     4. PROVIDED_ONLY             只提供本地或远程依赖项
     5. SUB_PROJECTS              只有子项目。
     6. SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
     7. TESTED_CODE               由当前变量(包括依赖项)测试的代码
     */
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> =
        TransformManager.SCOPE_FULL_PROJECT;

    override fun isIncremental() = false

    override fun transform(
        context: Context?,
        inputs: MutableCollection<TransformInput>?,
        referencedInputs: MutableCollection<TransformInput>?,
        outputProvider: TransformOutputProvider?,
        isIncremental: Boolean
    ) {
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
    }

    companion object {
        private const val NAME = "MyTransform"
    }
}