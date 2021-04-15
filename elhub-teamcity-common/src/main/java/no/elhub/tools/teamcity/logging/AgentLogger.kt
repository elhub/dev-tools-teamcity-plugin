package no.elhub.tools.teamcity.logging

import jetbrains.buildServer.log.Loggers
import no.elhub.tools.teamcity.Constants

/**
 * Logging class which outputs messages to TeamCity Agent Logs.
 */
class AgentLogger : Logger {

    override fun info(message: String) {
        Loggers.AGENT.info(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message))
    }

    override fun warn(message: String, e: Exception?) {
        Loggers.AGENT.warn(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message), e)
    }

    override fun error(message: String, e: Exception?) {
        Loggers.AGENT.error(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message), e)
    }

}