package eu.syplex.proxy.util

import com.google.common.reflect.TypeToken
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import ninja.leaping.configurate.ConfigurationNode

class ComponentTranslator(private val configurationNode: ConfigurationNode) {

    fun fromConfig(node: String): TextComponent {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw(node))
    }

    fun listFromConfig(node: String): ArrayList<TextComponent> {
        val components = ArrayList<TextComponent>()
        configurationNode.getNode(node).getList(TypeToken.of(String::class.java)).forEach { node ->
            components.add(LegacyComponentSerializer.legacyAmpersand().deserialize(node))
        }
        return components
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

    fun rawWithTwoReplacements(node: String, fP: String, sP: String, fR: String, sR: String): String {
        return raw(node).replace(fP, fR).replace(sP, sR)
    }

    fun raw(node: String): String {
        return configurationNode.getNode(node).getString(null)
    }


}
