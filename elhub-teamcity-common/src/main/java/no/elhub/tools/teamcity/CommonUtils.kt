package no.elhub.tools.teamcity

/**
 * A collection of helper methods that are used throughout the plugin
 *
 */
object CommonUtils {
    /**
     * Check if the given string is NULL or an empty string
     *
     * @param str String to be checked
     * @return Whether the string is NULL or empty
     */
    fun isNullOrEmpty(str: String?): Boolean {
        return str == null || str == ""
    }

    /**
     * Check if the given object is NULL
     *
     * @param obj Object to be checked for NULL
     * @return Whether the object is NULL
     */
    fun isNull(obj: Any?): Boolean {
        return obj == null
    }
}