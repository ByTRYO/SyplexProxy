package eu.syplex.proxy.util

import net.kyori.adventure.text.Component
import ninja.leaping.configurate.ConfigurationNode

class ComponentTranslator(private val configurationNode: ConfigurationNode) {
    fun fromConfig(node: String, child: String): Component {
        return Component.text(configurationNode.getNode(node).getString(child))
    }
}
