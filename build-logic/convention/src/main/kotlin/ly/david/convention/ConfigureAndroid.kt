package ly.david.convention

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

private const val COMPILE_SDK_VERSION = 35
private const val MIN_SDK_VERSION = 24
private const val TARGET_SDK_VERSION = COMPILE_SDK_VERSION

fun Project.configureAndroid() {
    android {
        compileSdkVersion(COMPILE_SDK_VERSION)

        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION

            manifestPlaceholders += mapOf("appAuthRedirectScheme" to "")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }

        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("desugarjdklibs").get())
        }
    }
}

private fun Project.android(configure: BaseExtension.() -> Unit) = extensions.configure(configure)
