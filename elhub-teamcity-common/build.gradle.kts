plugins {
    id ("com.github.rodm.teamcity-common")
}

description = "elhub-teamcity-common"

dependencies {
    implementation("org.apache.httpcomponents:httpclient:4.5.8")
    compileOnly("org.jetbrains.teamcity:common-api:2020.2")
    implementation(kotlin("stdlib-jdk8"))
}

teamcity {
    version = rootProject.extra["teamcityVersion"] as String
}
