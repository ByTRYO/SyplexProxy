package eu.syplex.proxy.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import ninja.leaping.configurate.ConfigurationNode

class ComponentTranslator(private val configurationNode: ConfigurationNode) {

    fun fromConfig(node: String, child: String): Component {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw(node, child))
    }

    fun fromConfigWithReplacement(node: String, child: String, placeholder: String, value: String): Component {
        val raw = raw(node, child)
        val replaced = raw.replace(placeholder, value)

        return LegacyComponentSerializer.legacyAmpersand().deserialize(replaced)
    }

    private fun raw(node: String, child: String): String {
        return configurationNode.getNode(node, child).getString(null)
    }

}
