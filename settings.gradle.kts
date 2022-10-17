rootProject.name = "elhub-teamcity-plugin"

include ("elhub-teamcity-common")
include ("elhub-teamcity-agent")
include ("elhub-teamcity-server")

pluginManagement {
    repositories {
        maven(url = "https://jfrog.elhub.cloud:443/artifactory/elhub-plugins")
    }
}
