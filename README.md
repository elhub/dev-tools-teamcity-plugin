> [!WARNING]  
> This project has been archived and is no longer being actively developed by Elhub.

# elhub-teamcity-plugin

[<img src="https://img.shields.io/badge/repo-github-blue" alt="">](https://github.com/elhub/devxp-elhub-teamcity-plugin)
[<img src="https://img.shields.io/badge/issues-jira-orange" alt="">](https://jira.elhub.cloud/issues/?jql=project%20%3D%20%22Team%20Dev%22%20AND%20component%20%3D%20devxp-elhub-teamcity-plugin%20AND%20status%20!%3D%20Done)
[<img src="https://teamcity.elhub.cloud/app/rest/builds/buildType:(id:DevXp_DevXpElhubTeamcityPlugin_PublishDocs)/statusIcon" alt="">](https://teamcity.elhub.cloud/project/DevXp_DevXpElhubTeamcityPlugin?mode=builds#all-projects)
[<img src="https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin&metric=alert_status" alt="">](https://sonar.elhub.cloud/dashboard?id=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin)
[<img src="https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin&metric=ncloc" alt="">](https://sonar.elhub.cloud/dashboard?id=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin)
[<img src="https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin&metric=bugs" alt="">](https://sonar.elhub.cloud/dashboard?id=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin)
[<img src="https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin&metric=vulnerabilities" alt="">](https://sonar.elhub.cloud/dashboard?id=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin)
[<img src="https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin&metric=coverage" alt="">](https://sonar.elhub.cloud/dashboard?id=no.elhub.devxp%3Adevxp-elhub-teamcity-plugin)

## About

This is a TeamCity plugin developed for Elhub's TeamCity installation. It comprises of a server and an agent plugin
that add a build feature to TeamCity for [Phabricator](https://www.phacility.com/phabricator/) code reviews.

Our typical use case is to have Phabricator trigger a build configuration in our TeamCity installation using the
[elhub-teamcity-plugin](https://github.com/elhub/dev-tools-elhub-teamcity-plugin) (typically our 
CodeReview workflow, which you can also see in the .teamcity sourcecode for this project). The build feature in
this project uses arcanist to pull in the changes from the differential revision that triggered the build
configuration. The build configuration then performs unit tests, linting and performs a sonar scans on the
repository (the results of which are posted on the differential revision as comment and lint messages) before
returning a success or fail back to Phabricator.

The code here is mostly a fork of the TeamCity-Phabricator-Plugin of
[x-lab-ltd](https://github.com/x-lab-ltd/Teamcity-Phabricator-Plugin) except for the gradle build instrumentation
and some minor tweaks and adjustments to better suit our systems.

## Getting Started

### Prerequisites

* TeamCity v2022.04 or later

### Installation

Gradle builds a zip (elhub-teamcity-plugin-<version>.zip) which we upload to our local package manager. This zip can
be downloaded and installed as a plugin in TeamCity.

## Usage

The phabricator-build-feature can be applied to a build configuration using the Kotlin DSL as follows:

```kotlin
features {

    feature {
        type = "phabricator-build-feature"
        param("plugin.phabricatorUrl", "https://phabricator.example.com")
        param("plugin.conduitToken", "%conduit-token%")
        param("plugin.pathToArc", "/usr/bin/arc")
    }

}
```

It can of course also be applied from the GUI.

Building the project is a simple matter of running

```shell
./gradle buildDist
```
## Testing

Explain how to run the automated tests for this system if appropriate.

```shell
./gradlew test
```

## Contributing

Contributing, issues and feature requests are welcome. See the
[Contributing](https://github.com/elhub/dev-tools-elhub-teamcity-plugin/blob/main/CONTRIBUTING.md) file.

## Owners

This project is developed by [Elhub](https://elhub.no). For the specific development group responsible for this
code, see the [Codeowners](https://github.com/elhub/dev-tools-elhub-teamcity-plugin/blob/main/CODEOWNERS) file.

## License

This project is [MIT](https://github.com/elhub/dev-tools-elhub-teamcity-plugin/blob/main/LICENSE.md) licensed.
