pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.19.2"
}

gradleEnterprise {
    buildCache {
        local {
            isEnabled = true
            removeUnusedEntriesAfterDays = 30
        }
        remote<HttpBuildCache> {
            isEnabled = false
        }
    }
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(!System.getenv("CI").isNullOrEmpty())
    }
}

rootProject.name = "MusicSearch"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":android:app",
    ":android:baselineprofile",
    ":android:feature:nowplaying",
    ":android:feature:spotify",
    ":desktop:app",
    ":core:coroutines",
    ":core:logging:api",
    ":core:logging:implementation",
    ":core:preferences",
    ":data:common:network",
    ":data:coverart",
    ":data:database",
    ":data:musicbrainz",
    ":data:repository",
    ":data:spotify",
    ":data:wikimedia",
    ":shared",
    ":shared:domain",
    ":shared:feature:collections",
    ":shared:feature:database",
    ":shared:feature:details",
    ":shared:feature:graph",
    ":shared:feature:history",
    ":shared:feature:images",
    ":shared:feature:licenses",
    ":shared:feature:search",
    ":shared:feature:settings",
    ":shared:feature:stats",
    ":shared:strings",
    ":test-data",
    ":ui:common",
    ":ui:core",
    ":ui:test:screenshot",
    ":konsist-test",
    ":test:image",
)
