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
    id("com.gradle.enterprise") version "3.16.2"
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
    ":core:parcelize",
    ":core:preferences",
    ":data:common:network",
    ":data:coverart",
    ":data:database",
    ":data:musicbrainz",
    ":data:repository",
    ":data:spotify",
    ":domain",
    ":feature:details",
    ":feature:stats",
    ":shared",
    ":shared:feature:history",
    ":shared:feature:search",
    ":shared:screens",
    ":strings",
    ":test-data",
    ":ui:collections",
    ":ui:common",
    ":ui:common-legacy",
    ":ui:core",
    ":ui:image",
    ":ui:test:image",
    ":ui:test:screenshot",
    ":ui:settings",
)
