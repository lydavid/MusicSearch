pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.15.1"
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
    ":app",
    ":desktop-app",

    ":data-android",
    ":data",
    ":data:core",
    ":data:common:network",
    ":data:coverart",
    ":data:database",
    ":data:spotify",
    ":data:musicbrainz",
    ":domain",
    ":feature:search",
    ":test-data",
    ":ui:collections",
    ":ui:common",
    ":ui:core",
    ":ui:history",
    ":ui:image",
    ":ui:nowplaying",
    ":ui:test:image",
    ":ui:test:screenshot",
    ":ui:settings",
    ":ui:spotify-broadcast-receiver",
    ":ui:stats",
)
