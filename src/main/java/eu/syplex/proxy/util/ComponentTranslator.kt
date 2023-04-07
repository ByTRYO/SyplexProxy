package eu.syplex.proxy.util

import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import ninja.leaping.configurate.ConfigurationNode

class ComponentTranslator(private val configurationNode: ConfigurationNode) {

    fun fromConfig(node: String): TextComponent {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw(node))
    }

    fun fromConfigWithReplacement(node: String, placeholder: String, replacement: String): TextComponent {
        val raw = raw(node)
        val replaced = raw.replace(placeholder, replacement)

        return LegacyComponentSerializer.legacyAmpersand().deserialize(replaced)
    }

    fun fromConfigWithTwoReplacements(node: String, fP: String, sP: String, fR: String, sR: String): TextComponent {
        val raw = raw(node)
        val replaced = raw.replace(fP, fR).replace(sP, sR)

        return LegacyComponentSerializer.legacyAmpersand().deserialize(replaced)
    }

    fun fromConfigWithThreeReplacements(node: String, fP: String, sP: String, tP: String, fR: String, sR: String, tR: String): TextComponent {
        val raw = raw(node)
        val replaced = raw.replace(fP, fR).replace(sP, sR).replace(tP, tR)

        return LegacyComponentSerializer.legacyAmpersand().deserialize(replaced)
    }

    fun raw(node: String): String {
        return configurationNode.getNode(node).getString(null)
    }

}
