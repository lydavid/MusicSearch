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
    ":android:app",
    ":android:feature:nowplaying",
    ":android:feature:spotify",
    ":desktop:app",
    ":core:coroutines",
    ":core:logging:api",
    ":core:logging:implementation",
    ":core:models",
    ":core:preferences",
    ":data",
    ":data:common:network",
    ":data:coverart",
    ":data:database",
    ":data:musicbrainz",
    ":data:repository",
    ":data:spotify",
    ":domain",
    ":feature:search",
    ":shared",
    ":strings",
    ":test-data",
    ":ui:collections",
    ":ui:common",
    ":ui:core",
    ":ui:history",
    ":ui:image",
    ":ui:test:image",
    ":ui:test:screenshot",
    ":ui:settings",
    ":ui:stats",
)
