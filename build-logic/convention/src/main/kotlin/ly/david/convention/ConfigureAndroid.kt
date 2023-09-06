package ly.david.convention

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

private const val COMPILE_SDK_VERSION = 34
private const val MIN_SDK_VERSION = 23
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
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}

private fun Project.android(configure: BaseExtension.() -> Unit) = extensions.configure(configure)
