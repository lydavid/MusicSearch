plugins {
    `kotlin-dsl`
}

dependencies {
    // TODO: toml. can we have both build-logic and app use the same toml?
    compileOnly("com.android.tools.build:gradle:8.0.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")
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
