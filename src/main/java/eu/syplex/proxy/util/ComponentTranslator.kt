package eu.syplex.proxy.util

import net.kyori.adventure.text.Component
import ninja.leaping.configurate.ConfigurationNode

class ComponentTranslator(private val configurationNode: ConfigurationNode) {
    fun fromConfig(node: String, child: String): Component {
        val content = configurationNode.getNode(node).getNode(child).getString(null)
        return Component.text(content)
    }
}
