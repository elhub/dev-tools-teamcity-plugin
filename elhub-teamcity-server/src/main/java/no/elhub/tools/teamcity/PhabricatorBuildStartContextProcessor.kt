package no.elhub.tools.teamcity

import jetbrains.buildServer.serverSide.BuildStartContext
import jetbrains.buildServer.serverSide.BuildStartContextProcessor

/**
 * At the start of a build gather the parameters the agent plugin requires and
 * set them in shared parameters. The agent can then read them from the shared
 * parameters.
 *
 */
class PhabricatorBuildStartContextProcessor : BuildStartContextProcessor {
    override fun updateParameters(context: BuildStartContext) {
        // Get build parameters and put them in a shared location for access on the
        // agent
        val parameters = context.build.buildOwnParameters
        if (parameters.containsKey(Constants.BUILD_ID)) {
            context.addSharedParameter(Constants.BUILD_ID, parameters[Constants.BUILD_ID]!!)
        }
        if (parameters.containsKey(Constants.DIFF_ID)) {
            context.addSharedParameter(Constants.DIFF_ID, parameters[Constants.DIFF_ID]!!)
        }
        if (parameters.containsKey(Constants.HARBORMASTER_PHID)) {
            context.addSharedParameter(Constants.HARBORMASTER_PHID, parameters[Constants.HARBORMASTER_PHID]!!)
        }
        if (parameters.containsKey(Constants.REVISION_ID)) {
            context.addSharedParameter(Constants.REVISION_ID, parameters[Constants.REVISION_ID]!!)
        }
    }
}