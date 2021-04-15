# Project Name

<!-- PROJECT SHIELDS -->
![TeamCity Build](https://teamcity.elhub.cloud/app/rest/builds/buildType:(id:Common_CommonBuildConfiguration_AutoRelease)/statusIcon)
[![Quality Gate Status](https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin&metric=alert_status)](https://sonar.elhub.cloud/dashboard?id=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin)
[![Lines of Code](https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin&metric=ncloc)](https://sonar.elhub.cloud/dashboard?id=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin)

[![Vulnerabilities](https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin&metric=vulnerabilities)](https://sonar.elhub.cloud/dashboard?id=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin)
[![Bugs](https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin&metric=bugs)](https://sonar.elhub.cloud/dashboard?id=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin)
[![Code Smells](https://sonar.elhub.cloud/api/project_badges/measure?project=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin&metric=code_smells)](https://sonar.elhub.cloud/dashboard?id=no.elhub.tools%3Adev-tools-elhub-teamcity-plugin)

## Table of Contents

* [About](#about)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Testing](#testing)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [Owners](#owners)
* [License](#license)


## About

This is a TeamCity plugin developed for Elhub's TeamCity installation. It comprises of a server and an agent plugin
that add a build feature to TeamCity for [Phabricator](https://www.phacility.com/phabricator/) code reviews.

Our typical use case is to have Phabricator trigger a build configuration in our TeamCity installation using the
[elhub-phabricator-extensions](https://github.com/elhub/dev-tools-elhub-phabricator-extensions) (typically our 
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

* TeamCity v2020.2 or later

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

## Roadmap

See the [open issues](https://jira.elhub.cloud/issues/?jql=project%20%3D%20TD%20AND%20component%20%3D%20elhub-teamcity-plugin%20AND%20resolution%20%3D%20Unresolved) for a list of proposed features (and known issues).

## Contributing

Contributing, issues and feature requests are welcome. See the
[Contributing](https://github.com/elhub/dev-tools-elhub-teamcity-plugin/blob/main/CONTRIBUTING.md) file.

## Owners

This project is developed by [Elhub](https://elhub.no). For the specific development group responsible for this
code, see the [Codeowners](https://github.com/elhub/dev-tools-elhub-teamcity-plugin/blob/main/CODEOWNERS) file.

## License

This project is [MIT](https://github.com/elhub/dev-tools-elhub-teamcity-plugin/blob/main/LICENSE.md) licensed.
