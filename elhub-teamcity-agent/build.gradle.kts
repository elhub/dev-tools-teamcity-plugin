plugins {
    id ("com.github.rodm.teamcity-agent")
}

description = "elhub-teamcity-plugin-agent"

dependencies {
    implementation(project(":elhub-teamcity-common"))
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("org.jetbrains.teamcity:agent-api:2020.2")
    testImplementation("org.jetbrains.teamcity:tests-support:2020.2")
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
