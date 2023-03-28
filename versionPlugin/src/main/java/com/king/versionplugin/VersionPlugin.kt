package com.king.versionplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * https://docs.gradle.org/current/userguide/composite_builds.html
 *
 * @author VanceKing
 * @since 2021/11/22
 */
class VersionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        //project.extensions.findByType(BaseExtension::class.java)?.registerTransform(MyTransform())
    }

    companion object {

    }
}