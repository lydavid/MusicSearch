pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
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
    ":shared:feature:nowplaying",
    ":shared:feature:spotify",
    ":desktop:app",
    ":core:logging:api",
    ":core:logging:implementation",
    ":core:preferences",
    ":data:common:network",
    ":data:coverart",
    ":data:database",
    ":data:listenbrainz",
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
    ":shared:feature:listens",
    ":shared:feature:search",
    ":shared:feature:settings",
    ":shared:feature:stats",
    ":test-data",
    ":ui:common",
    ":ui:test:screenshot",
    ":konsist-test",
    ":test:image",
)
