package ly.david.convention

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

private const val COMPILE_SDK_VERSION = 33
private const val MIN_SDK_VERSION = 23
private const val TARGET_SDK_VERSION = 33

fun Project.configureAndroid() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(COMPILE_SDK_VERSION)

        defaultConfig {
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
