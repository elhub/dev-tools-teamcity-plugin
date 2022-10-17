plugins {
    //kotlin("jvm") version "1.7.10" apply false
    id("no.elhub.devxp.kotlin-core") version "0.0.1"
    id("com.github.rodm.teamcity-server") version "1.3.2"
}

repositories {
    maven("https://jfrog.elhub.cloud/artifactory/elhub-mvn")
    maven("https://teamcity.elhub.cloud/app/dsl-plugins-repository")
}

description = "Implement automated semantic release for gradle, maven and ansible projects."
