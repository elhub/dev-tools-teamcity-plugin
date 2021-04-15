package no.elhub.tools.teamcity.logging

/**
 * Interface to the logger
 */
interface Logger {

    fun info(message: String)

    fun warn(message: String, e: Exception? = null)

    fun error(message: String, e: Exception? = null)

}