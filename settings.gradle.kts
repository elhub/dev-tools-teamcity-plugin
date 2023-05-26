@file:Suppress("UnstableApiUsage")

rootProject.name = "elhub-teamcity-plugin"

include("elhub-teamcity-common")
include("elhub-teamcity-agent")
include("elhub-teamcity-server")

pluginManagement {
    repositories {
        maven(url = "https://jfrog.elhub.cloud:443/artifactory/elhub-plugins")
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://jfrog.elhub.cloud:443/artifactory/elhub-mvn")
    }

    versionCatalogs {
        create("libs") {
            from("no.elhub.devxp:devxp-version-catalog:0.4.3")
        }
    }
}
