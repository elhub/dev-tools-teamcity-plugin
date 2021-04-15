package no.elhub.tools.teamcity

import jetbrains.buildServer.agent.AgentLifeCycleAdapter
import jetbrains.buildServer.agent.AgentLifeCycleListener
import jetbrains.buildServer.agent.AgentRunningBuild
import jetbrains.buildServer.agent.BuildProgressLogger
import jetbrains.buildServer.util.EventDispatcher
import no.elhub.tools.teamcity.logging.AgentLogger

/**
 * Extend the AgentLifeCycleAdapter to check for builds which have the
 * Phabricator build feature enabled. If enabled then patch the updated code
 * using arcanist making sure the build is against these changed sources.
 *
 */
class AgentBuildExtension(
    eventDispatcher: EventDispatcher<AgentLifeCycleListener?>,
    phabLogger: AgentLogger
) : AgentLifeCycleAdapter() {
    private val agentLogLogger: AgentLogger
    private var buildLogger: BuildProgressLogger? = null
    private val phabricatorConfig: PhabricatorPluginConfig
    private var phabricatorTriggeredBuild = false

    /**
     * From the associated parameters determine if the phabricator build feature is
     * enabled and we have all the information to successfully patch changes from
     * the phabricator differential revision
     */
    override fun buildStarted(runningBuild: AgentRunningBuild) {
        // Reset the check for a phabricator build
        phabricatorTriggeredBuild = false

        // Setup logger to print to build output
        buildLogger = runningBuild.buildLogger

        // Attempt to get the parameters set by the phabricator build feature. If non
        // are set then the feature is not turned on.
        val phabricatorBuildFeatureParameters = runningBuild
            .getBuildFeaturesOfType(Constants.BUILD_FEATURE_TYPE)
        if (phabricatorBuildFeatureParameters.isEmpty()) {
            return
        }

        // Check that the relevant build and phabricator settings/configurations are
        // present
        val params: MutableMap<String?, String> = HashMap()
        params.putAll(phabricatorBuildFeatureParameters.iterator().next().parameters)
        params.putAll(runningBuild.sharedConfigParameters)

        // Setup plugin specific configuration
        phabricatorConfig.setParameters(params)

        // Now we have set all the parameters we need to check if
        // everything is present and correct for us to continue
        if (!phabricatorConfig.isPluginSetup) {
            agentLogLogger.info("Plugin incorrectly configured")
            return
        }
        phabricatorTriggeredBuild = true
        agentLogLogger.info("Plugin ready")
        buildLogger!!.message("Active")
        buildLogger!!.message("${Constants.PHABRICATOR_URL_SETTING}: ${phabricatorConfig.phabricatorURL.toString()}")
        buildLogger!!.message("${Constants.PHABRICATOR_ARCANIST_PATH_SETTING}: ${phabricatorConfig.pathToArcanist}")
        buildLogger!!.message("${Constants.BUILD_ID}: ${phabricatorConfig.buildId}")
        buildLogger!!.message("${Constants.DIFF_ID}: ${phabricatorConfig.diffId}")
        buildLogger!!.message("${Constants.HARBORMASTER_PHID}: ${phabricatorConfig.harbormasterPHID}")
        buildLogger!!.message("${Constants.REVISION_ID}: ${phabricatorConfig.revisionId}")
        runningBuild.addSharedEnvironmentVariable("PHABRICATOR_URI", phabricatorConfig.phabricatorURL.toString())
        runningBuild.addSharedEnvironmentVariable("PHABRICATOR_BUILD_ID", phabricatorConfig.buildId!!)
        runningBuild.addSharedEnvironmentVariable("PHABRICATOR_DIFF_ID", phabricatorConfig.diffId!!)
        runningBuild.addSharedEnvironmentVariable("PHABRICATOR_HARBORMASTER_PHID", phabricatorConfig.harbormasterPHID!!)
        runningBuild.addSharedEnvironmentVariable("PHABRICATOR_REVISION_ID", phabricatorConfig.revisionId!!)
    }

    /**
     * Once the sources have been updated via the native TeamCity process check if
     * the Phabricator build feature is enabled and use arcanist to patch in changes
     * from a associated phabricator differential revision.
     */
    override fun sourcesUpdated(runningBuild: AgentRunningBuild) {
        if (!phabricatorTriggeredBuild) {
            return
        }
        buildLogger!!.message(String.format(Constants.LOGGING_PREFIX_TEMPLATE, "Attempting arc patch"))
        agentLogLogger.info("Attempting arc patch")
        val arcanistClient = ArcanistClient(
            phabricatorConfig.pathToArcanist,
            runningBuild.checkoutDirectory.path, phabricatorConfig.phabricatorURL.toString(),
            phabricatorConfig.conduitToken, agentLogLogger
        )
        val patchCode = arcanistClient.patch(phabricatorConfig.diffId)
        agentLogLogger.info(String.format("Arc patch exited with code: %s", patchCode))
        if (patchCode > 0) {
            runningBuild.stopBuild("Patch failed to apply. Check the agent output log for patch failure detals.")
            agentLogLogger.info("Patch failed to apply, stopping build")
            return
        }
        buildLogger!!.message(String.format(Constants.LOGGING_PREFIX_TEMPLATE, "Patch completed"))
        agentLogLogger.info("Patch completed")
    }

    init {
        eventDispatcher.addListener(this)
        agentLogLogger = phabLogger
        phabricatorConfig = PhabricatorPluginConfig()
        phabricatorConfig.setLogger(agentLogLogger)
    }
}