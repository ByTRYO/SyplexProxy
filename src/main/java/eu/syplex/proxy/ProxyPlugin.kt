package eu.syplex.proxy

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.backend.PlayerTracker
import eu.syplex.proxy.backend.database.sadu.StaticDataLoader
import eu.syplex.proxy.backend.listener.ChatListener
import eu.syplex.proxy.backend.listener.PostLoginListener
import eu.syplex.proxy.command.*
import eu.syplex.proxy.config.ConfigLoader
import eu.syplex.proxy.config.PropertyLoader
import eu.syplex.proxy.util.CommandPool
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.JoinMeValidator
import eu.syplex.proxy.config.news.NewsFetcher
import eu.syplex.proxy.config.news.NewsLoader
import ninja.leaping.configurate.ConfigurationNode
import java.nio.file.Path
import java.util.logging.Logger

@Plugin(id = "proxy", name = "Proxy", version = "1.0-SNAPSHOT", authors = ["Merry", "ByTRYO"])
class ProxyPlugin @Inject constructor(val proxyServer: ProxyServer, logger: Logger, @DataDirectory private val dataDirectory: Path) {

    private val configurationNode: ConfigurationNode = ConfigLoader(dataDirectory).configurationNode
    private val translator: ComponentTranslator = ComponentTranslator(configurationNode)
    private val playerTracker: PlayerTracker = PlayerTracker()
    private val validator: JoinMeValidator = JoinMeValidator(this, proxyServer, translator)
    private val newsLoader: ConfigurationNode = NewsLoader(dataDirectory).configurationNode
    private val newsFetcher: NewsFetcher = NewsFetcher(newsLoader)

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        StaticDataLoader.connect(PropertyLoader(configurationNode).loadConnectionProperty())
        registerCommands()
        registerListener()
    }

    private fun registerCommands() {
        val pool = CommandPool(this)
        pool.register(PingCommand(translator), "ping")
        pool.register(YouTubeCommand(translator), "youtube", "yt")
        pool.register(KickCommand(proxyServer, translator, playerTracker), "kick")
        pool.register(BanCommand(proxyServer, translator, configurationNode, playerTracker), "bann", "ban")
        pool.register(UnbanCommand(translator, playerTracker), "unban")
        pool.register(MuteCommand(translator, proxyServer, playerTracker, configurationNode), "mute")
        pool.register(UnmuteCommand(translator, playerTracker), "unmute")
        pool.register(TeamChatCommand(proxyServer, translator), "teamchat", "tc")
        pool.register(JoinMeCommand(translator, validator, proxyServer), "joinme")
        pool.register(JoinMeAcceptCommand(translator, validator), "accept")
        pool.register(NewsCommand(translator, newsFetcher), "news")
        pool.register(HelpCommand(translator), "help", "hilfe", "?")
        pool.register(CheckCommand(translator, playerTracker), "check")
    }

    private fun registerListener() {
        proxyServer.eventManager.register(this, PostLoginListener(playerTracker, translator))
        proxyServer.eventManager.register(this, ChatListener(playerTracker, translator))
    }

}