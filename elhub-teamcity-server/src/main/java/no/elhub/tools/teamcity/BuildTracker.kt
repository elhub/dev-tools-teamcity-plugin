package no.elhub.tools.teamcity

import jetbrains.buildServer.messages.Status
import jetbrains.buildServer.serverSide.SRunningBuild
import no.elhub.tools.teamcity.logging.ServerLogger
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

/**
 * Class that is run in a thread once a build has started. If the build does not
 * have the phabricator build feature then the tread should come to an end
 * otherwise wait for the build to finish and report back to phabricator
 *
 */
class BuildTracker(private val build: SRunningBuild, private val logger: ServerLogger) : Runnable {
    private val phabricatorConfig: PhabricatorPluginConfig
    override fun run() {
        // Attempt to get the parameters set by the phabricator build feature. If non
        // are set then the feature is not turned on.
        val phabricatorBuildFeatureParameters = build
            .getBuildFeaturesOfType(Constants.BUILD_FEATURE_TYPE)

        // Check if the build is part of a configuration which
        // uses the phabricator build feature.
        if (!phabricatorBuildFeatureParameters.isEmpty()) {
            logger.info("Tracking build " + build.buildNumber)

            // Gather together all the build and phabricator parameters
            val params: MutableMap<String?, String> = HashMap()
            params.putAll(build.buildOwnParameters)
            params.putAll(phabricatorBuildFeatureParameters.iterator().next().parameters)

            // Setup plugin specific configuration
            phabricatorConfig.setParameters(params)

            // Now we have set all the parameters we need to check if
            // everything is present and correct for us to continue
            if (!phabricatorConfig.isPluginSetup) {
                logger.info("Plugin incorrectly configured")
                return
            }
            while (!build.isFinished) {
                // Wait until the build finishes
            }
            logger.info(String.format("Build %s finished: %s", build.buildNumber, build.buildStatus))
            val harbormasterBuildStatus = parseTeamCityBuildStatus(build.buildStatus)
            notifyHarbormaster(harbormasterBuildStatus)
        }
    }

    /**
     * Compose and dispatch an API call to harbormaster to notify of the build
     * status
     */
    private fun notifyHarbormaster(buildStatus: String) {
        val phabricatorURL = phabricatorConfig.phabricatorURL
        try {
            HttpClients.createDefault().use { client ->
                val requestEndpoint = String.format("%s/api/harbormaster.sendmessage", phabricatorURL.toString())
                logger.info(String.format("Sending build status to: %s", requestEndpoint))
                val httpPost = HttpPost(requestEndpoint)
                val params: MutableList<NameValuePair> = ArrayList()
                params.add(BasicNameValuePair("api.token", phabricatorConfig.conduitToken))
                params.add(BasicNameValuePair("buildTargetPHID", phabricatorConfig.harbormasterPHID))
                params.add(BasicNameValuePair("type", buildStatus))
                httpPost.entity = UrlEncodedFormEntity(params)
                client.execute(httpPost).use { response ->
                    val responseText = EntityUtils.toString(response.entity)
                    logger.info(String.format("Phabricator response: %s", responseText))
                }
            }
        } catch (exception: Exception) {
            // Just catching any Exception because the request to Phabricator may have
            // failed and we should investigate
            logger.error("Request to harbormaster failed", exception)
        }
    }

    private fun parseTeamCityBuildStatus(buildFinishedStatus: Status): String {
        return if (buildFinishedStatus.isSuccessful) {
            "pass"
        } else "fail"
    }

    init {
        phabricatorConfig = PhabricatorPluginConfig()
        phabricatorConfig.setLogger(logger)
    }
}