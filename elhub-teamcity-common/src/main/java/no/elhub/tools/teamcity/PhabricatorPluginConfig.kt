package no.elhub.tools.teamcity

import no.elhub.tools.teamcity.logging.Logger
import java.net.MalformedURLException
import java.net.URL

/**
 * Holds all the parameters set on the build applied by the harbormaster trigger
 * from phabricator. It also has the methods used to communicate the build
 * result back to harbormaster
 *
 * @author steven.cooney
 */
class PhabricatorPluginConfig {
    private var logger: Logger? = null
    private var params: Map<String?, String>? = null

    // Build Feature Variables
    var phabricatorURL: URL? = null
        private set
    var conduitToken: String? = null
        private set
    var pathToArcanist: String? = null
        private set

    // Harbormaster Variables
    var buildId: String? = null
        private set
    var diffId: String? = null
        private set
    var harbormasterPHID: String? = null
        private set
    var revisionId: String? = null
        private set

    /**
     * Set the appropriate logger depending if the class is called from the SERVER
     * or AGENT
     *
     * @param logger
     */
    fun setLogger(logger: Logger?) {
        this.logger = logger
    }

    /**
     * Take a copy of all build parameters which will then be parsed
     *
     * @param parameters
     */
    fun setParameters(parameters: Map<String?, String>?) {
        // Clear the class variables to avoid any previous build configurations staying
        // around
        clearParameters()
        params = parameters
        logger!!.info("Looking for parameters")
        for (param in params!!.keys) {
            if (!CommonUtils.isNullOrEmpty(param)) {
                logger!!.info(String.format("Found %s", param))
                when (param) {
                    Constants.PHABRICATOR_URL_SETTING -> {
                        logger!!.info(String.format("Found Phabrictor URL: %s", params!![Constants.PHABRICATOR_URL_SETTING]))
                        try {
                            phabricatorURL = URL(params!![Constants.PHABRICATOR_URL_SETTING])
                        } catch (e: MalformedURLException) {
                            logger!!.warn(
                                String.format(
                                    "Failed to parse phabricator URL: %s",
                                    params!![Constants.PHABRICATOR_URL_SETTING]
                                ), e
                            )
                        }
                    }
                    Constants.PHABRICATOR_CONDUIT_TOKEN_SETTING -> {
                        logger!!.info("Found Phabricator Conduit Token")
                        conduitToken = params!![Constants.PHABRICATOR_CONDUIT_TOKEN_SETTING]
                    }
                    Constants.PHABRICATOR_ARCANIST_PATH_SETTING -> {
                        logger!!.info(
                            String.format(
                                "Found Phabricator Arcanist Path: %s",
                                params!![Constants.PHABRICATOR_ARCANIST_PATH_SETTING]
                            )
                        )
                        pathToArcanist = params!![Constants.PHABRICATOR_ARCANIST_PATH_SETTING]
                    }
                    Constants.BUILD_ID -> {
                        logger!!.info(String.format("Found build id: %s", params!![Constants.BUILD_ID]))
                        buildId = params!![Constants.BUILD_ID]
                    }
                    Constants.DIFF_ID -> {
                        logger!!.info(String.format("Found diff ID: %s", params!![Constants.DIFF_ID]))
                        diffId = params!![Constants.DIFF_ID]
                    }
                    Constants.HARBORMASTER_PHID -> {
                        logger!!.info(
                            String.format(
                                "Found harbormaster target PHID: %s",
                                params!![Constants.HARBORMASTER_PHID]
                            )
                        )
                        harbormasterPHID = params!![Constants.HARBORMASTER_PHID]
                    }
                    Constants.REVISION_ID -> {
                        logger!!.info(String.format("Found revision ID: %s", params!![Constants.REVISION_ID]))
                        revisionId = params!![Constants.REVISION_ID]
                    }
                    else -> {
                    }
                }
            }
        }
    }

    /**
     * A little method than can be used to clear all the parameters we want to set
     */
    private fun clearParameters() {
        phabricatorURL = null
        conduitToken = null
        pathToArcanist = null
        buildId = null
        diffId = null
        harbormasterPHID = null
        revisionId = null
    }

    /**
     * Check that all the required parameters are set and we can continue to attempt
     * patching changes from Arcanist
     *
     * @return Result of whether or not the plugin has the appropriate parameter to
     * be used by an agent.
     */
    val isPluginSetup: Boolean
        get() = (!CommonUtils.isNull(phabricatorURL) && !CommonUtils.isNull(pathToArcanist) && !CommonUtils.isNullOrEmpty(buildId) &&
                !CommonUtils.isNullOrEmpty(diffId) && !CommonUtils.isNullOrEmpty(harbormasterPHID) && !CommonUtils.isNullOrEmpty(revisionId))

}