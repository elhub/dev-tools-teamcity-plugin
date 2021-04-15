import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig

plugins {
    kotlin("jvm") version "1.4.31" apply false
    id("com.github.ben-manes.versions") version "0.36.0"
    id("com.github.rodm.teamcity-server") version "1.3.2"
    id("com.jfrog.artifactory") version "4.18.3"
    id("maven-publish") apply true
}

extra["teamcityVersion"] = findProperty("teamcity.version") ?: "2020.2"
val mavenPubName = "teamCityPlugin"

description = "Implement automated semantic release for gradle, maven and ansible projects."

repositories {
    maven("https://jfrog.elhub.cloud/artifactory/elhub-mvn")
    maven("https://teamcity.elhub.cloud/app/dsl-plugins-repository")
}

subprojects {
    apply(plugin = "kotlin")
}

/*
 * Publishing
 */
publishing {
    publications {
        create<MavenPublication>(mavenPubName) {
            artifact("elhub-teamcity-server/build/distributions/elhub-teamcity-server-$version.zip") {
                artifactId = "elhub-teamcity-plugin"
                extension = "zip"
            }
        }
    }
}

artifactory {
    setContextUrl("https://jfrog.elhub.cloud/artifactory")
    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<groovy.lang.GroovyObject> {
            setProperty("repoKey", project.findProperty("binaryrepo") ?: "elhub-bin-test-local")
            setProperty("username", project.findProperty("mavenuser") ?: "nouser")
            setProperty("password", project.findProperty("mavenpass") ?: "nopass")
        })
        defaults(delegateClosureOf<groovy.lang.GroovyObject> {
            invokeMethod("publications", mavenPubName)
            setProperty("publishArtifacts", true)
            setProperty("publishPom", false)
        })
    })
    resolve(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig> {
        setProperty("repoKey", "repo")
    })
}

tasks.get("artifactoryPublish").dependsOn(tasks.get("assemble"))

tasks.get("publish").dependsOn(tasks.get("artifactoryPublish"))
