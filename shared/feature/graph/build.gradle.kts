plugins {
    id("ly.david.android.library")
    id("ly.david.musicsearch.compose.multiplatform")
    id("ly.david.musicsearch.kotlin.multiplatform")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "ly.david.musicsearch.shared.feature.graph"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.models)
                implementation(projects.core.preferences)
                implementation(projects.shared.domain)
                implementation(projects.ui.common)
                implementation(projects.ui.image)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                implementation(libs.circuit.foundation)
                implementation(libs.koin.core)
                implementation(libs.paging.common)
                implementation(libs.paging.compose)
                implementation(libs.kotlinx.datetime)

//                implementation("io.data2viz.d2v:d2v-color:0.10.7") {
//                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
//                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
//                }
//                implementation("io.data2viz.d2v:d2v-force:0.10.7")
//                implementation("io.data2viz.d2v:d2v-geo:0.10.7") {
//                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
//                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
//                }
//                implementation("io.data2viz.d2v:d2v-random:0.10.7") {
//                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
//                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
//                }
//                implementation("io.data2viz.d2v:d2v-viz:0.10.7")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(projects.ui.test.screenshot)
                implementation(libs.junit.jupiter.engine)
                implementation(libs.kotlinx.coroutines.test)
                implementation("app.cash.turbine:turbine:1.1.0")
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    debugImplementation(compose.uiTooling)
}
