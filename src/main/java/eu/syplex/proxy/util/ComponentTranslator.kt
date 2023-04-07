package eu.syplex.proxy.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import ninja.leaping.configurate.ConfigurationNode

class ComponentTranslator(private val configurationNode: ConfigurationNode) {

    fun fromConfig(node: String): Component {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw(node))
    }

    fun fromConfigWithReplacement(node: String, placeholder: String, replacement: String): Component {
        val raw = raw(node)
        val replaced = raw.replace(placeholder, replacement)

        return LegacyComponentSerializer.legacyAmpersand().deserialize(replaced)
    }

    fun raw(node: String): String {
        return configurationNode.getNode(node).getString(null)
    }

}
