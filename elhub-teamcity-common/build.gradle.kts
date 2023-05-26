plugins {
    id("no.elhub.devxp.kotlin-library")
    id("com.github.rodm.teamcity-common")
}

description = "elhub-teamcity-common"

dependencies {
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    compileOnly("org.jetbrains.teamcity:common-api:${rootProject.extra["teamcityVersion"]}")
    implementation(libs.kotlin.stdlib.jdk8)
}

teamcity {
    version = rootProject.extra["teamcityVersion"] as String
}
