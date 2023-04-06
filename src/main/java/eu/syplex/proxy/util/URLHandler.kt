package eu.syplex.proxy.util

import ninja.leaping.configurate.ConfigurationNode

class URLHandler(private val configurationNode: ConfigurationNode) {

    fun fromConfig(child: String): String {
        return configurationNode.getNode("urls", child).getString(null)
    }

}