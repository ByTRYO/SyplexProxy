package eu.syplex.proxy.util

import java.util.*

object StringUtil {

    fun <T : LinkedList<String>> copyPartialMatches(token: String, originals: Iterable<String>, collection: T): T {
        for (string in originals) {
            if (startsWithIgnoreCase(string, token)) {
                collection.add(string)
            }
        }
        return collection
    }

    private fun startsWithIgnoreCase(string: String, prefix: String): Boolean {
        if (string.length < prefix.length) return false
        return string.regionMatches(0, prefix, 0, prefix.length, true)
    }

}