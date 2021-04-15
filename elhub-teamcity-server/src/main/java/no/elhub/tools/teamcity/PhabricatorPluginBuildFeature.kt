package no.elhub.tools.teamcity

import jetbrains.buildServer.serverSide.BuildFeature
import jetbrains.buildServer.web.openapi.PluginDescriptor
import no.elhub.tools.teamcity.logging.ServerLogger

/**
 * Sets up a Build Feature for the phabricator plugin. Ultimately allowing the
 * plugin to be configured through the TeamCity BuildFeature UI.
 *
 */
class PhabricatorPluginBuildFeature(
    pluginDescriptor: PluginDescriptor,
    phabLogger: ServerLogger
) : BuildFeature() {
    private val myEditUrl: String
    private val logger: ServerLogger
    override fun getType(): String {
        return Constants.BUILD_FEATURE_TYPE
    }

    override fun getDisplayName(): String {
        return Constants.PHABRICATOR_FEATURE_DISPLAY_NAME
    }

    override fun getEditParametersUrl(): String? {
        return myEditUrl
    }

    override fun isMultipleFeaturesPerBuildTypeAllowed(): Boolean {
        return false
    }

    /**
     * Populates the Parameters Description on the build features page
     */
    override fun describeParameters(params: Map<String, String>): String {
        var url = ""
        for ((key, value) in params) {
            if (key == Constants.PHABRICATOR_URL_SETTING) {
                url = value
            }
        }
        return String.format("Phabricator URL: %s", url)
    }

    init {
        myEditUrl = pluginDescriptor.getPluginResourcesPath("phabricatorBuildFeature.jsp")
        logger = phabLogger
        logger.info("Build feature registered")
    }
}