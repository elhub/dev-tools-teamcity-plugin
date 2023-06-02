plugins {
    id("no.elhub.devxp.kotlin-core") version "0.1.2"
    id("com.github.rodm.teamcity-server") version "1.5"
}

repositories {
    maven("https://jfrog.elhub.cloud/artifactory/elhub-mvn")
    maven("https://teamcity.elhub.cloud/app/dsl-plugins-repository")
}

description = "Implement automated semantic release for gradle, maven and ansible projects."
