package com.king.versionplugin

import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters

/**
 *
 * @author VanceKing
 * created at 2023/3/28
 */
class MyTransformActionA : TransformAction<MyParams> {
    override fun getParameters(): MyParams {
        return MyParams()
    }


    override fun transform(p0: TransformOutputs) {

    }
}

class MyParams : TransformParameters