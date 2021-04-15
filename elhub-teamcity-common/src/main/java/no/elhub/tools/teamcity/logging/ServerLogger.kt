package no.elhub.tools.teamcity.logging

import jetbrains.buildServer.log.Loggers
import no.elhub.tools.teamcity.Constants

/**
 * Logging class which outputs messages to TeamCity Server Logs.
 */
class ServerLogger : Logger {

    override fun info(message: String) {
        Loggers.SERVER.info(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message))
    }

    override fun warn(message: String, e: Exception?) {
        Loggers.SERVER.warn(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message), e)
    }

    override fun error(message: String, e: Exception?) {
        Loggers.SERVER.error(String.format(Constants.LOGGING_PREFIX_TEMPLATE, message), e)
    }

}