plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
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
        register("androidCompose") {
            id = "ly.david.android.compose"
            implementationClass = "ly.david.convention.plugin.AndroidComposeConventionPlugin"
        }
        register("kotlinJvm") {
            id = "ly.david.kotlin"
            implementationClass = "ly.david.convention.plugin.KotlinJvmConventionPlugin"
        }
    }
}
