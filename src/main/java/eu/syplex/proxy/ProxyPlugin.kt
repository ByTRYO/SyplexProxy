package eu.syplex.proxy

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.command.PingCommand
import eu.syplex.proxy.command.YouTubeCommand
import eu.syplex.proxy.config.ConfigLoader
import eu.syplex.proxy.util.ComponentTranslator
import java.nio.file.Path
import java.util.logging.Logger

@Plugin(id = "proxy", name = "Proxy", version = "1.0-SNAPSHOT", authors = ["Merry", "ByTRYO"])
class ProxyPlugin @Inject constructor(private val proxyServer: ProxyServer, private val logger: Logger, @DataDirectory private val dataDirectory: Path) {

    private val translator: ComponentTranslator

    init {
        val configurationNode = ConfigLoader(dataDirectory).configurationNode
        translator = ComponentTranslator(configurationNode)

        logger.info("Plugin was enabled successfully")
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        val commandManager= proxyServer.commandManager

        val pingCommandMeta = commandManager.metaBuilder("ping").plugin(this).build()
        commandManager.register(pingCommandMeta, PingCommand(translator))

        val youTubeCommandMeta = commandManager.metaBuilder("youtube").aliases("yt").plugin(this).build()
        commandManager.register(youTubeCommandMeta, YouTubeCommand())
    }

}