package no.elhub.tools.teamcity

import no.elhub.tools.teamcity.logging.AgentLogger

/**
 * Wrapper for arcanist and patching in changes from differential revisions
 *
 */
class ArcanistClient(
    private val arcPath: String?,
    private val workingDir: String,
    private val conduitAPI: String,
    private val token: String?,
    private val log: AgentLogger
) {

    /**
     * Run "arc patch" using arcanist on the build agent to pull in changes from a differential revision.
     *
     * @param diffId The identifying diff which has the changes to patch within differential
     * @return The exit code for "arc patch" or 1 if exceptions occur
     */
    fun patch(diffId: String?): Int {
        try {
            val commandToExecute = CommandBuilder().setCommand(arcPath).setAction("patch")
                .setWorkingDir(workingDir).setArg("--diff").setArg(diffId)
                .setArg("--force")
                .setFlagWithValueEquals("--conduit-uri", conduitAPI)
                .setFlagWithValueEquals("--conduit-token", token).build()
            log.info(String.format("Running Command: %s", commandToExecute.toString()))
            return commandToExecute.executeAndWait()
        } catch (e: IllegalArgumentException) {
            log.warn("Building command failed", e)
        } catch (e: Exception) {
            log.warn("Patching error", e)
        }
        return 1
    }
}