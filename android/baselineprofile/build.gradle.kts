import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("ly.david.android.test")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "ly.david.musicsearch.baselineprofile"

    targetProjectPath = ":android:app"

    flavorDimensions += listOf("appStore")
    productFlavors {
        create("fDroid") { dimension = "appStore" }
        create("googlePlay") { dimension = "appStore" }
    }

    // This code creates the gradle managed device used to generate baseline profiles.
    // To use GMD please invoke generation through the command line:
    // ./gradlew :android:app:generateBaselineProfile
    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6Api34") {
            device = "Pixel 6"
            apiLevel = 34
            systemImageSource = "google"
        }
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    managedDevices += "pixel6Api34"
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

androidComponents {
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "io.github.lydavid.musicsearch",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId.orEmpty() },
        )
    }
}
