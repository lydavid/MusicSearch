package ly.david.convention.plugin

import ly.david.convention.configureAndroid
import ly.david.convention.configureDetekt
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            configureAndroid()
            configureDetekt()
        }
    }
}
