package no.elhub.tools.teamcity

/**
 * Build up the command which will be executed on the agent during relevant
 * builds
 *
 */
class CommandBuilder {
    private var command: String? = null
    private var action: String? = null
    private var workingDir: String? = null
    private val args: MutableList<String?> = ArrayList()

    /**
     * Set the directory which the command should be run from.
     *
     * @param workingDir The directory the command should be run from.
     * @return The builder instance so things can be chained.
     */
    fun setWorkingDir(workingDir: String?): CommandBuilder {
        require(!CommonUtils.isNullOrEmpty(workingDir)) { "Need to provide valid working directory" }
        this.workingDir = workingDir
        return this
    }

    /**
     * Set the command/executable that will be run.
     *
     * @param cmd The command to be executed.
     * @return The builder instance so things can be chained.
     */
    fun setCommand(cmd: String?): CommandBuilder {
        require(!CommonUtils.isNullOrEmpty(cmd)) { "Need to provide a valid command" }
        command = cmd
        return this
    }

    /**
     * Set the action the command will perform this comes immediately after the
     * executable. E.g git pull where pull is the action.
     *
     * @param action
     * @return The builder instance so things can be chained.
     */
    fun setAction(action: String?): CommandBuilder {
        require(!CommonUtils.isNullOrEmpty(action)) { "Need to provide a valid action" }
        this.action = action
        return this
    }

    /**
     * Set arguments that will be added to the command which will be executed
     *
     * @param arg An argument to be added to the command
     * @return The builder instance so things can be chained.
     */
    fun setArg(arg: String?): CommandBuilder {
        require(!CommonUtils.isNullOrEmpty(arg)) { "Need to provide a valid argument" }
        args.add(arg)
        return this
    }

    /**
     * Set arguments which take the form of a key value pair. E.g token=12344
     *
     * @param key   The initial part of the argument
     * @param value The value of the argument to be added after an the key and
     * equals
     * @return The builder instance so things can be chained.
     */
    fun setFlagWithValueEquals(key: String?, value: String?): CommandBuilder {
        args.add(String.format("%s=%s", key, value))
        return this
    }

    /**
     * Using the values set on this builder create a Command object than can be
     * executed.
     *
     * @return Command which and be executed at a later point
     */
    fun build(): Command {
        require(!CommonUtils.isNullOrEmpty(command)) { "Must provide a command" }
        args.add(0, command)
        if (!CommonUtils.isNullOrEmpty(action)) {
            args.add(1, action)
        }
        return Command(args.toTypedArray(), workingDir)
    }
}