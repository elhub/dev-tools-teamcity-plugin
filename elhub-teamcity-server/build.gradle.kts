import com.github.rodm.teamcity.TeamCityEnvironment

plugins {
    id("no.elhub.devxp.kotlin-library")
    id("com.github.rodm.teamcity-server")
}

description = "elhub-teamcity-plugin-server"

dependencies {
    implementation(project(":elhub-teamcity-common"))
    implementation(kotlin("stdlib-jdk8"))
    agent (project(path = ":elhub-teamcity-agent", configuration = "plugin"))
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    compileOnly("org.jetbrains.teamcity:server-api:${rootProject.extra["teamcityVersion"]}")
    compileOnly("org.jetbrains.teamcity:server-web-api:${rootProject.extra["teamcityVersion"]}")
    testImplementation("org.jetbrains.teamcity:tests-support:${rootProject.extra["teamcityVersion"]}")
}

extra["downloadsDir"] = project.findProperty("downloads.dir") ?: "${rootDir}/downloads"
extra["serversDir"] = project.findProperty("servers.dir") ?: "${rootDir}/servers"
extra["java8Home"] = project.findProperty("java8.home") ?: "/opt/jdk1.8.0_92"

val agent = configurations.getByName("agent")

teamcity {
    version = rootProject.extra["teamcityVersion"] as String

    server {
        descriptor {
            name = "elhub-teamCity-plugin"
            displayName = "Elhub TeamCity Plugin"
            version = rootProject.version as String?
            vendorName = "Elhub"
            vendorUrl = "https://elhub.no"
            description = "TeamCity plugin developed to be used on Elhub's teamcity architecture. "
            email = "teamdev@elhub.no"
            useSeparateClassloader = true
        }
    }

}
