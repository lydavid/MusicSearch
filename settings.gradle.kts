pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.13.4"
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
    ":data-android",
    ":data",
    ":data:base",
    ":data:coverart",
    ":data:domain",
    ":data:spotify",
    ":data:room",
    ":data:musicbrainz",
    ":test-data",
    ":ui:collections",
    ":ui:common",
    ":ui:core",
    ":ui:history",
    ":ui:image",
    ":ui:test:image",
    ":ui:test:screenshot",
    ":ui:settings",
)
