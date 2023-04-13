package eu.syplex.proxy.util.news

import com.google.common.reflect.TypeToken
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import ninja.leaping.configurate.ConfigurationNode

class NewsFetcher(private val configurationNode: ConfigurationNode) {

    fun getNews(): List<TextComponent> {
        val components = ArrayList<TextComponent>()
        configurationNode.getNode("news").getList(TypeToken.of(String::class.java)).forEach { node ->
            components.add(LegacyComponentSerializer.legacyAmpersand().deserialize(node))
        }
        return components
    }

}


