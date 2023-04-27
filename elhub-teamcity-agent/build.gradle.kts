plugins {
    id("no.elhub.devxp.kotlin-library")
    id ("com.github.rodm.teamcity-agent")
}

description = "elhub-teamcity-plugin-agent"

dependencies {
    implementation(project(":elhub-teamcity-common"))
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("org.jetbrains.teamcity:agent-api:${rootProject.extra["teamcityVersion"]}")
    testImplementation("org.jetbrains.teamcity:tests-support:${rootProject.extra["teamcityVersion"]}")
}

teamcity {
    version = rootProject.extra["teamcityVersion"] as String

    agent {
        descriptor {
            pluginDeployment {
                useSeparateClassloader = true
            }
        }
    }
}
