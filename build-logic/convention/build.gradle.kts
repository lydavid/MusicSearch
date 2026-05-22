plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "ly.david.android.application"
            implementationClass = "ly.david.convention.plugin.AndroidApplicationConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "ly.david.musicsearch.compose.multiplatform"
            implementationClass = "ly.david.convention.plugin.ComposeMultiplatformConventionPlugin"
        }
        register("kotlinMultiplatform") {
            id = "ly.david.musicsearch.kotlin.multiplatform"
            implementationClass = "ly.david.convention.plugin.KotlinMultiplatformLibraryConventionPlugin"
        }
        register("parcelize") {
            id = "ly.david.parcelize"
            implementationClass = "ly.david.convention.plugin.ParcelizeConventionPlugin"
        }
    }
}
