package ly.david.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

const val COMPILE_SDK_VERSION = 36
private const val MIN_SDK_VERSION = 24

fun Project.configureAndroid() {
    android {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION

            manifestPlaceholders += mapOf("appAuthRedirectScheme" to "")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
//                isCoreLibraryDesugaringEnabled = true
        }

//            dependencies {
//                add("coreLibraryDesugaring", libs.findLibrary("desugarjdklibs").get())
//            }
    }
}

private fun Project.android(configure: ApplicationExtension.() -> Unit) = extensions.configure(configure)
