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

                //    implementation("io.data2viz.d2v:d2v-axis:0.10.7")
//    implementation("io.data2viz.d2v:d2v-chord:0.10.7")
                implementation("io.data2viz.d2v:d2v-color:0.10.7") {
                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
                }
//    implementation("io.data2viz.d2v:d2v-contour:0.10.7")
//    implementation("io.data2viz.d2v:d2v-delaunay:0.10.7")
//    implementation("io.data2viz.d2v:d2v-dsv:0.10.7")
//    implementation("io.data2viz.d2v:d2v-ease:0.10.7")
                implementation("io.data2viz.d2v:d2v-force:0.10.7")
//    implementation("io.data2viz.d2v:d2v-format:0.10.7")
                implementation("io.data2viz.d2v:d2v-geo:0.10.7") {
                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
                }
//    implementation("io.data2viz.d2v:d2v-hexbin:0.10.7")
//    implementation("io.data2viz.d2v:d2v-hierarchy:0.10.7")
//    implementation("io.data2viz.d2v:d2v-quadtree:0.10.7")
                implementation("io.data2viz.d2v:d2v-random:0.10.7") {
                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
                    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
                }
//    implementation("io.data2viz.d2v:d2v-scale:0.10.7")
//    implementation("io.data2viz.d2v:d2v-shape:0.10.7")
//    implementation("io.data2viz.d2v:d2v-tile:0.10.7")
//    implementation("io.data2viz.d2v:d2v-time:0.10.7")
//    implementation("io.data2viz.d2v:d2v-timer:0.10.7")
                implementation("io.data2viz.d2v:d2v-viz:0.10.7")
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
            }
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}
