package no.elhub.tools.teamcity

/**
 * A set of constants that are used throughout the server and agent plugins
 *
 */
object Constants {
    const val PHABRICATOR_FEATURE_DISPLAY_NAME = "Phabricator Reporter"
    const val LOGGING_PREFIX_TEMPLATE = "Elhub TeamCity Plugin - %s"

    // Build Feature Settings
    const val BUILD_FEATURE_TYPE = "phabricator-build-feature"
    const val PHABRICATOR_URL_SETTING = "plugin.phabricatorUrl"
    const val PHABRICATOR_CONDUIT_TOKEN_SETTING = "plugin.conduitToken"
    const val PHABRICATOR_ARCANIST_PATH_SETTING = "plugin.pathToArc"

    // Build Config
    const val BUILD_ID = "phabricator.BUILD_ID"
    const val DIFF_ID = "phabricator.DIFF_ID"
    const val HARBORMASTER_PHID = "phabricator.HARBORMASTER_TARGET_PHID"
    const val REVISION_ID = "phabricator.REVISION_ID"
}