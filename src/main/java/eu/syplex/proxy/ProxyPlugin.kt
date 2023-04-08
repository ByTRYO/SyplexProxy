package eu.syplex.proxy

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.database.sadu.StaticDataLoader
import eu.syplex.proxy.backend.listener.PostLoginListener
import eu.syplex.proxy.command.PingCommand
import eu.syplex.proxy.command.YouTubeCommand
import eu.syplex.proxy.config.ConfigLoader
import eu.syplex.proxy.config.PropertyLoader
import eu.syplex.proxy.util.CommandPool
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Notifier
import ninja.leaping.configurate.ConfigurationNode
import java.nio.file.Path
import java.util.logging.Logger

@Plugin(id = "proxy", name = "Proxy", version = "1.0-SNAPSHOT", authors = ["Merry", "ByTRYO"])
class ProxyPlugin @Inject constructor(val proxyServer: ProxyServer, logger: Logger, @DataDirectory private val dataDirectory: Path) {

    private val configurationNode: ConfigurationNode = ConfigLoader(dataDirectory).configurationNode
    private val translator: ComponentTranslator = ComponentTranslator(configurationNode)
    private val notifier: Notifier = Notifier(proxyServer, translator)
    private val playerTracker: PlayerTracker = PlayerTracker()

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
//        StaticDataLoader.connect(PropertyLoader(configurationNode).loadConnectionProperty())
        registerCommands()
        registerListener()
    }

    private fun registerCommands() {
        val pool = CommandPool(this)
        pool.register(PingCommand(translator), "ping")
        pool.register(YouTubeCommand(translator), "youtube", "yt")
    }

    private fun registerListener() {
        proxyServer.eventManager.register(this, PostLoginListener(notifier, playerTracker))
    }

}