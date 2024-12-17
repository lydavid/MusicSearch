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
        register("androidLibrary") {
            id = "ly.david.android.library"
            implementationClass = "ly.david.convention.plugin.AndroidLibraryConventionPlugin"
        }
        register("androidTest") {
            id = "ly.david.android.test"
            implementationClass = "ly.david.convention.plugin.AndroidTestConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "ly.david.musicsearch.compose.multiplatform"
            implementationClass = "ly.david.convention.plugin.ComposeMultiplatformConventionPlugin"
        }
        register("kotlinJvm") {
            id = "ly.david.kotlin"
            implementationClass = "ly.david.convention.plugin.KotlinJvmConventionPlugin"
        }
        register("kotlinMultiplatform") {
            id = "ly.david.musicsearch.kotlin.multiplatform"
            implementationClass = "ly.david.convention.plugin.KotlinMultiplatformConventionPlugin"
        }
    }
}
