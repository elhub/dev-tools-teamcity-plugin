package no.elhub.tools.teamcity

import jetbrains.buildServer.serverSide.BuildServerAdapter
import jetbrains.buildServer.serverSide.BuildServerListener
import jetbrains.buildServer.serverSide.SRunningBuild
import jetbrains.buildServer.util.EventDispatcher
import no.elhub.tools.teamcity.logging.ServerLogger

/**
 * Listen for builds to start by extending BuildServerAdapter. When a build
 * starts spin up a thread of BuildTracker class to follow the progress of the
 * build.
 *
 */
class PhabricatorBuildServerAdapter(
    buildServerListener: EventDispatcher<BuildServerListener?>,
    phabLogger: ServerLogger
) : BuildServerAdapter() {
    private val logger: ServerLogger
    override fun buildStarted(runningBuild: SRunningBuild) {
        super.buildStarted(runningBuild)

        // Do the work in a separate thread to avoid blocking
        // other builds monitored by this adapter (this might be
        // a tad overkill)
        Thread(BuildTracker(runningBuild, logger)).start()
    }

    init {
        buildServerListener.addListener(this)
        logger = phabLogger
        logger.info("Build server adapter registered")
    }
}