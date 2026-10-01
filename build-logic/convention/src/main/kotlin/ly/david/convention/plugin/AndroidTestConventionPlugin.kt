package ly.david.convention.plugin

import ly.david.convention.configureDetekt
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
            }

//            configureAndroidTest()
            configureDetekt()
        }
    }
}
//
//private fun Project.configureAndroidTest() {
//    android {
//        compileSdk = COMPILE_SDK_VERSION
////            compileSdkVersion(COMPILE_SDK_VERSION)
//
//        defaultConfig {
////                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//
////            minSdk = ManifestSystemProperty.UsesSdk.MIN_SDK_VERSION
////            targetSdk = TARGET_SDK_VERSION
//
////                manifestPlaceholders += mapOf("appAuthRedirectScheme" to "")
//        }
//
//        compileOptions {
//            sourceCompatibility = JavaVersion.VERSION_21
//            targetCompatibility = JavaVersion.VERSION_21
////                isCoreLibraryDesugaringEnabled = true
//        }
////
////            dependencies {
////                add("coreLibraryDesugaring", libs.findLibrary("desugarjdklibs").get())
////            }
//    }
//}
//
//private fun Project.android(configure: TestExtension.() -> Unit) = extensions.configure(configure)