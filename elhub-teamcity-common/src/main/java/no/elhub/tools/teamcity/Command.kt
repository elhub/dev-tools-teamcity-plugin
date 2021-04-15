package no.elhub.tools.teamcity

import java.io.File

/**
 * Wrapper for executing a command on the host system
 */
class Command(executableAndArguments: Array<String?>, workingDirectory: String?) {
    private val workingDir = if (CommonUtils.isNullOrEmpty(workingDirectory)) null else File(workingDirectory)
    private val fullCommand: Array<String?>
    private val processBuilder: ProcessBuilder
    private var process: Process? = null

    /**
     * Execute the command and wait for it to finish gathering the exit code of the
     * process
     *
     * @return The exit code from the command which has been executed.
     * @throws Exception If there is an exception when running the command throw to
     * allowing the executing class to deal with error handling.
     */
    @Throws(Exception::class)
    fun executeAndWait(): Int {
        // May well fail and throw an exception but we will handle that in the agent
        // plugin.
        process = processBuilder.start()
        return process!!.waitFor()
    }

    /**
     * Output the command and working directory for this class. It should only be
     * used sparingly.
     */
    override fun toString(): String {
        if (fullCommand.size < 1) {
            return "Command not setup"
        }
        val printableCommand = java.lang.String.join(" ", *fullCommand)

        // The workingDir might not have been explicitly set
        return if (processBuilder.directory() == null) {
            String.format("Command: %s", printableCommand)
        } else String.format("WorkingDir: %s - Command: %s", processBuilder.directory().toString(), printableCommand)
    }

    /**
     * Compose the command which is to be executed
     *
     * @param executableAndArguments An array of the command to be executed.
     * @param workingDirectory       The directory the command will be executed
     * from.
     */
    init {
        fullCommand = executableAndArguments

        // ProcessBuilder requires the full command to be an array with the
        // executable the first element;
        processBuilder = ProcessBuilder(*executableAndArguments)
        processBuilder.directory(workingDir)
        processBuilder.inheritIO()
    }
}